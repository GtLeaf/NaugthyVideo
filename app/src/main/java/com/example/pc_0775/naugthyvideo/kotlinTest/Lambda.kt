package main

import java.io.File

fun main(args:Array<String>){
    val text = File("src/Resource/input.txt").readText()
    print(text)
}