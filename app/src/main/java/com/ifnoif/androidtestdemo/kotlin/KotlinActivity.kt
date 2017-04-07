package com.ifnoif.androidtestdemo.kotlin

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.ifnoif.androidtestdemo.kotlin.toast;
import com.ifnoif.androidtestdemo.R
import kotlinx.android.synthetic.main.activity_kotlin.*
import kotlinx.android.synthetic.main.dialog_kotlin.view.*

class KotlinActivity : AppCompatActivity() {

    val text:String = "hell world!";
    var message:String? = "this is a dialog message!";

    companion object {
        var sText = "companion text";
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        test.setOnClickListener { view-> onClickTest(view) }
    }

//    fun Context.toast(message: String, time: Int) {
//        Toast.makeText(this, message, time).show();
//    }

    fun onClickTest(view: View):String?{
        var context: Context = this;
        context.toast("toast text",Toast.LENGTH_SHORT);

        return null;
    }

    fun onClickDialog(dialog:DialogInterface,which:Int, text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    class StaticInnerClass {
        var name: String? = "";
        constructor(name:String){
            this.name = name;
        }

        override fun toString(): String {
            return "name:"+name;
        }

        open fun getNewName():String?{
            return this.name;
        }
    }

    var parentVar:String = "parent var";

    open class Food(name: String){
        lateinit var mName:String;
        init {
            mName = name;
            mName = KotlinActivity().parentVar;
        }

        constructor(nameStr: String, weight: Int) : this(nameStr) {
            //没有加？表示不可为空
            var a:String = "test";
            a = "test1"

            //以下可以为空
            var b:String?="test";
            b = "test1"
            b = null



//            var length:Int? = b.length;//编译提示错误
            var length2:Int? = b?.length;

            var newB:String = b!!;//强制转换时，如果为空，则抛出KotlinNullPointerException
            var length4:Int = b.length;//b已经强制转换成非空了，所以这里不会报错

            test(b!!);
            test2(b);

            var object1: StaticInnerClass? = null;
            var length3: String = object1?.getNewName() ?: throw RuntimeException("test");
        }

        fun test(name:String) {

        }

        fun test2(name:String?) {

        }
    }
}

