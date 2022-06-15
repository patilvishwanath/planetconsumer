package com.raywenderlich.android.ui

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import raywenderlich.android.R
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

  private lateinit var button: Button
  private lateinit var editText: EditText
  private lateinit var listView: ListView
  private var cursor: Cursor? = null

  var URI = Uri.parse("")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    listView = findViewById(R.id.listview)
    editText = findViewById(R.id.edittext)
    button = findViewById(R.id.button)

    button.setOnClickListener {
      // TODO
    }

    refresh_name_table()
  }

  private fun refresh_name_table() {

  }

  private fun insertPlanetName() {
    val contentValues = ContentValues(1)
    contentValues.put("name", editText.text.toString())
    contentResolver.insert(URI, contentValues)
  }

  private fun tryToReadBases() {
    cursor = contentResolver.query(URI, null, null, null, null)
    listView.adapter = SimpleCursorAdapter(this,
        android.R.layout.simple_list_item_2,
        cursor,
        arrayOf("rebel_base_lat", "rebel_base_lng"),
        intArrayOf(android.R.id.text1, android.R.id.text2),
        0
    )
  }

  private fun hackToFindRebelBaseWithQuery() {
    var lat = 0f
    var lng = 0f
    var latMargin = 45f
    var lngMargin = 90f

    while (latMargin > 0.0005) {

      cursor = contentResolver.query(URI, null, "name = '" + editText.text.toString() + "' and rebel_base_lat > " + lat, null, null)

      if (cursor != null && cursor!!.moveToNext()) {
        lat += latMargin
      } else {
        lat -= latMargin
      }

      latMargin /= 2
    }

    while (lngMargin > 0.0005) {

      cursor = contentResolver.query(URI, null, "name = '" + editText.text.toString() + "' and rebel_base_lng > " + lng, null, null)

      if (cursor != null && cursor!!.moveToNext()) {
        lng += lngMargin
      } else {
        lng -= lngMargin
      }

      lngMargin /= 2
    }

    val format = DecimalFormat("#.###")
    val msg = "rebel base on " + editText.text.toString() + " is at lat " + format.format(lat.toDouble()) + ", lng " + format.format(lng.toDouble())
    Log.e("MYTEST", msg)
    Toast.makeText(this@MainActivity,
        msg,
        Toast.LENGTH_LONG)
        .show()
  }

  private fun hackToFindRebelBaseWithUpdate() {
    val contentValues = ContentValues(1)
    contentValues.put("name", editText.text.toString())

    var lat = 0f
    var lng = 0f
    var latMargin = 45f
    var lngMargin = 90f

    while (latMargin > 0.0005) {

      val changed = contentResolver.update(URI,
          contentValues, "name = '" + editText.text.toString() + "' and rebel_base_lat > " +
          lat, null)

      if (changed > 0) {
        lat += latMargin
      } else {
        lat -= latMargin
      }

      latMargin /= 2
    }

    while (lngMargin > 0.0005) {

      val changed = contentResolver.update(URI,
          contentValues, "name = '" + editText.text.toString() + "' and rebel_base_lng > " +
          lng, null)

      if (changed > 0) {
        lng += lngMargin
      } else {
        lng -= lngMargin
      }

      lngMargin /= 2
    }

    val format = DecimalFormat("#.###")
    val msg = "rebel base on " + editText.text.toString() + " is at lat " + format.format(lat.toDouble()) + ", lng " + format.format(lng.toDouble())
    Log.e("MYTEST", msg)
    Toast.makeText(this@MainActivity,
        msg,
        Toast.LENGTH_LONG)
        .show()
  }
}
