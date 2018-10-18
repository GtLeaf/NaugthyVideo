package main
enum class Lang(val hello:String){
    ENGLISH("Hello"),
    CHINESE("你好"),
    JAPANESE("こんにちは"),
    KOREAN("안녕하세요.");

    fun sayHello(){
        print(hello)
    }
    //构造方法
    init {

    }

    //伴生对象
    companion object {
        fun parse(name: String): Lang {
            return Lang.valueOf(name.toUpperCase())
        }
    }
}

fun main(vararg args:String){
    if (0 == args.size)return
    val lang = Lang.parse(args[0])
    println(lang)
    lang.sayHello()
    lang.sayBye()
}
//扩展方法
fun Lang.sayBye(){
    val bye = when(this){
        Lang.ENGLISH -> "bye"
        Lang.CHINESE -> "再见"
        Lang.JAPANESE -> "さような"
        Lang.KOREAN -> "안녕히 가세요"
    }
    print(bye)
}