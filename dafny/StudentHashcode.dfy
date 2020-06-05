function abs(x : int) : int {
  if (x < 0) then -x else x
}


method Abs(x : int) returns (r : int) 
  ensures r == abs(x)
  ensures x < 0 ==> r == -x;
  ensures x >= 0 ==> r == x; 
{
  if (x < 0) {
    r := -x;
  } else {
    r := x;
  }
}

method Main() {
  var v := Abs(3);
  assert v <= 3;
}