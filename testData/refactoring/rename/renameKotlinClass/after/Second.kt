package testing.rename2

import testing.rename.Third
import testing.*
import java.util.ArrayList

// Extends testing.rename.Third
public class Second : Third() {
  val temp : testing.rename.Third = Third()

  fun tempName(param : Third) : rename.Third {
    val local = Third()
    val otherLocal = param
    val arr = ArrayList<Third>()

    return testing.rename.Third()
  }
}