package main

fun main(args: Array<out String>):Unit{
    /*args.map{
        println(it)
    }*/

    args.map (::println)

    for (arg in args){
        println(arg)
    }
}