import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

  private Calculator calc = new Calculator ();

  @Test
  public void addTest(){
    Assertions.assertEquals (5, calc.add(2,3));
    Assertions.assertEquals (9, calc.add(4,5));
  }

  @Test
  public void subtractTest(){
    Assertions.assertEquals (3, calc.subtract(7, 4));
    Assertions.assertEquals (12, calc.subtract(20,8));
  }

  @Test
  public void powerTest(){
    Assertions.assertEquals (16, calc.power(4, 2));
    Assertions.assertEquals (27, calc.power(3, 3));
  }

  @Test
  public void powerTest_PositiveValueZeroPower(){
    Assertions.assertEquals (1, calc.power(3, 0));
  }

  @Test
  public void powerTest_PositiveValueNegativePower(){
    Assertions.assertEquals (.0625, calc.power(4, -2));
  }

  @Test
  public void powerTest_NegativeValuePositivePower(){
    Assertions.assertEquals (9, calc.power(-3, 2));
  }

  @Test
  public void powerTest_NegativeValueZeroPower(){
    Assertions.assertEquals (1, calc.power(-3, 0));
  }

  @Test
  public void powerTest_NegativeValueNegativePower(){
    Assertions.assertEquals (.04, calc.power(-5, -2));
  }

  @Test
  public void powerTest_ZeroValuePositivePower(){
    Assertions.assertEquals (0, calc.power(0, 1));
  }

  @Test
  public void powerTest_ZeroValueZeroPower(){
    Assertions.assertEquals (1, calc.power(0, 0));
  }

  @Test
  public void powerTest_ZeroValueNegativePower(){
    Assertions.assertEquals (Double.POSITIVE_INFINITY, calc.power(0, -1));
  }
}
