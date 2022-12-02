package com.duangs.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signal.apply {
            setConnected(false)
            setSignalLevel(level)
        }

        signal.Builder().setSignalLevel(3).build()

        connectSwitch.setOnClickListener {
            signal.setConnected(connectSwitch.isChecked)
        }

        resetBtn.setOnClickListener {
            try {
                signal.setSignalLevel(level)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }


        levelEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                level = try {
                    s.toString().toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    level
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}
