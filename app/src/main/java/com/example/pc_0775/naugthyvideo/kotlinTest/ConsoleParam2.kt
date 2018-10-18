package main
/*
* a_b_c_d e_f_g_h_i_j
* */
fun main(vararg args: String){
    args.flatMap {
        it.split("_")
    }.map {
        print("$it ${it.length}")
    }
}