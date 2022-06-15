package com.razeware.planetconsumer

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader

import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

  lateinit var button: Button
  lateinit var editText: EditText
  lateinit var listView: ListView
  var cursor: Cursor? = null

  var URI = Uri.parse("")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    listView = findViewById<ListView>(R.id.listview)
    editText = findViewById<EditText>(R.id.edittext)
    button = findViewById<Button>(R.id.button)

    button.setOnClickListener(View.OnClickListener {
      // TODO
    })

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

      latMargin = latMargin / 2
    }

    while (lngMargin > 0.0005) {

      cursor = contentResolver.query(URI, null, "name = '" + editText.text.toString() + "' and rebel_base_lng > " + lng, null, null)

      if (cursor != null && cursor!!.moveToNext()) {
        lng += lngMargin
      } else {
        lng -= lngMargin
      }

      lngMargin = lngMargin / 2
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

      latMargin = latMargin / 2
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

      lngMargin = lngMargin / 2
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
