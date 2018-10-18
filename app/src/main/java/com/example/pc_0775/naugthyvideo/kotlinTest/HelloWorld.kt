package main

fun main(arg: Array<out String>){
    var user: User = User(0, "candy")
    val sumLambda: (Int, Int) -> Int = {x,y -> x+y}
//    print(main.sum(1, 2))
    vars(1, 2, 3, 4, 5)
    println("\n"+sumLambda(1, 2))

    //声明变量 var <标识符> : <类型> = <初始化值>
    var x = 5   // 系统自动推断变量类型为Int
    x += 1      // 变量可修改

    //声明常量 val <标识符> : <类型> = <初始化值>
    val a: Int = 1
    val b = 1       // 系统自动推断变量类型为Int
    val c: Int      // 如果不在声明时初始化则必须提供变量类型
    c = 1           // 明确赋值

    //字符串模板
    var d = 1
    // 模板中的简单名称：
    val s1 = "a is $d"

    d = 2
    // 模板中的任意表达式：
    val s2 = "${s1.replace("is", "was")}, but now is $d"
    println(s2)

    //NULL检查机制
    //类型后面加?表示可为空
    var age: String? = "23"
    //抛出空指针异常
    val ages = age!!.toInt()
    //不做处理返回 null
    val ages1 = age?.toInt()
    //age为空返回-1
    val ages2 = age?.toInt() ?:-1

}

data class User(val id:Int, val name:String);


fun sum(a: Int, b: Int): Int{
    return a + b;
}

fun sum2(a: Int, b: Int) = a + b

public fun sum3(a: Int, b: Int): Int = a + b // public 方法则必须明确写出返回类型

//无返回值
fun printSum(a: Int, b: Int): Unit{
    print(a + b)
}

// 如果是返回 Unit类型，则可以省略(对于public方法也是这样)
fun printSum2(a: Int, b: Int){
    print(a + b);
}

public fun printSum3(a: Int, b: Int){
    print(a+b)
}

//可变长参数函数
fun vars(vararg v:Int){
    for (vt in v){
        print(vt)
    }
}
