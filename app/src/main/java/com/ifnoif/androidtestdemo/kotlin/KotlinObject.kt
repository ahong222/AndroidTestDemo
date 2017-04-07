package com.ifnoif.androidtestdemo.kotlin

import java.io.Serializable

/**
 * Created by shen on 17/4/4.
 */

class KotlinObject {
    var name: String? = null;
    var testName = "test"
    lateinit var sex: String;

    constructor() {
        sex = "未知";
    }

    constructor(name: String) {
        this.name = name;
        this.sex = "男";
        val food:Food = Food("apple");
    }

    data class Car(var name:String,var color:String, var weight:Int):Serializable {
    }

    fun testCar(){
        var car:Car = Car("bus","red",1000);
        var color = car.color;
        var name = car.name;
        var weight = car.weight;
        var newCar:Car =car.copy(name="bicycle",weight = 10);
        var equalsResult =car.equals(newCar);

    }


    override fun toString(): String {
        return "name:" + name + " sex:" + sex;
    }

    open class Food(name: String){
        lateinit var mName:String;
        init {
            mName = name;
        }
    }

    open class Animal(var nameStr: String) {
        open fun test(){
            val testName = nameStr;
        }
    }

    class Duck : Animal("鸭子") {
        override fun toString(): String {
            return "name:"+nameStr;
        }
    }


    class Dog(var name: String) : Animal(nameStr = name) {
        override fun test(){
            val testName = nameStr;
            this.name ="";
        }
    }

    class Pig(name: String, weight: Int) : Animal(nameStr = name) {

    }
}