// "Make variable mutable" "true"
class A() {
    val a: Int = 0
        <caret>set(v: Int) {}
}
