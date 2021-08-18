package com.hide09.androidfundamental.activityevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import com.hide09.androidfundamental.R



class MoveForResultActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnChoose: Button
    private lateinit var rgAnimal: RadioGroup

    companion object {
        const val EXTRA_SELECTED_VALUE = "extra_selected_value"
        const val RESULT_CODE = 110
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_for_result)

        btnChoose = findViewById(R.id.btn_choose)
        rgAnimal = findViewById(R.id.rg_animal)

        btnChoose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_choose){
            if (rgAnimal.checkedRadioButtonId > 0){
                var value = ""
                when(rgAnimal.checkedRadioButtonId){
                    R.id.rb_kelinci -> value = "Kelinci"
                    R.id.rb_panda -> value = "Panda"
                    R.id.rb_kucing -> value = "Kucing"
                    R.id.rb_ikan -> value = "Ikan"
                }
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_SELECTED_VALUE,value)
                setResult(RESULT_CODE, resultIntent)
                finish()
            }
        }
    }
}