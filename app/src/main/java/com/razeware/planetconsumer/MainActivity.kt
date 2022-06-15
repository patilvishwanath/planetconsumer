/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.razeware.planetconsumer

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

  private lateinit var button: Button
  private lateinit var editText: EditText
  private lateinit var listView: ListView
  private var cursor: Cursor? = null

  var URI = Uri.parse("content://com.razeware.planetprovider")
  var nameUri = Uri.withAppendedPath(URI, "name")
  var rebelBaseUri = Uri.withAppendedPath(URI, "rebel_base")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    listView = findViewById(R.id.listview)
    editText = findViewById(R.id.edittext)
    button = findViewById(R.id.button)

    button.setOnClickListener {
      hackToFindRebelBaseWithQuery()
    }

    refreshNameTable()
  }

  private fun refreshNameTable() {
    cursor = contentResolver.query(nameUri, null, null, null, null)


    if (cursor != null) {
      listView.adapter = SimpleCursorAdapter(this,
          android.R.layout.simple_list_item_1,
          cursor,
          arrayOf("name"),
          intArrayOf(android.R.id.text1),
          0
      )
    }
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
