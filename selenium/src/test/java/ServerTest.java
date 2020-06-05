import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerTest {
  private Server server;
  private WebDriver driver;
  private WebElement todoInput;

  private final List<String> todoTestList = Arrays.asList(
          "1",
          "2",
          "3",
          "4",
          "5",
          "6");

  @BeforeEach
  void setup() throws IOException {
    server = new Server();
    server.start(8080);

    driver = new ChromeDriver ();
    driver.get("http://localhost:" + server.getPort ());

    todoInput = driver.findElement (By.tagName ("input"));

    for(String todo: todoTestList){
      submitTodo(todo);
    }
  }

  @AfterEach
  void teardown(){
    try{
      Thread.sleep(3000);
    } catch (InterruptedException e){
      e.printStackTrace ();
    }
    driver.quit ();
    server.stop();
  }

  private void submitTodo(String todoText){
    todoInput.sendKeys (todoText);
    todoInput.submit ();


    try{
      Thread.sleep(10);
    } catch (InterruptedException e){
      e.printStackTrace ();
    }

    todoInput.clear ();
  }

  private List<String> getTextForElements(List<WebElement> webElements){
    List<String> strings = new ArrayList<> (webElements.size());

    for(WebElement webElement : webElements){
      strings.add(webElement.getText ());
    }

    return strings;
  }

  @Test
  void testAddTodos(){

    List<WebElement> foundTodoElements = driver.findElements (By.className("todo-text"));
    Assertions.assertEquals (todoTestList.size(), foundTodoElements.size());

    List<String> foundTodoStrings = getTextForElements(foundTodoElements);

    for(String expectedTodo : todoTestList){
      Assertions.assertTrue (foundTodoStrings.contains (expectedTodo),"Missing todo: " + expectedTodo);
    }

  }

  @Test
  void testMarkAndShowCompleted(){
    List<String> expectedCompletedItems = Arrays.asList("1", "2");

    for(String completedItem : expectedCompletedItems){
      WebElement completedItemCheckbox = findCheckboxForTodoItemByText(completedItem);
      Assertions.assertNotNull (completedItemCheckbox);
      completedItemCheckbox.click ();
    }

    WebElement showCompletedButton = driver.findElement (By.id("show-completed"));
    Assertions.assertNotNull(showCompletedButton);
    showCompletedButton.click();

    List<WebElement> displayedTodoElements = driver.findElements (By.className("todo-text"));
    Assertions.assertEquals (expectedCompletedItems.size (), displayedTodoElements.size());

    List<String> displayedElementText = getTextForElements (displayedTodoElements);
    for(String expectedCompletedItem : expectedCompletedItems){
      Assertions.assertTrue (displayedElementText.contains (expectedCompletedItem),
              "Expected item " + expectedCompletedItem + " not displayed");

    }
  }

  @Test
  void testDeleteCompleted(){
    List<String> expectedCompletedItems = Arrays.asList("1", "2", "3", "4", "5", "6");

    for(String completedItem : expectedCompletedItems){
      WebElement completedItemCheckbox = findCheckboxForTodoItemByText(completedItem);
      Assertions.assertNotNull (completedItemCheckbox);
      completedItemCheckbox.click ();
    }

    WebElement showCompletedButton = driver.findElement (By.id("delete-completed"));
    Assertions.assertNotNull(showCompletedButton);
    showCompletedButton.click();

    List<WebElement> foundTodoElements = driver.findElements (By.className("todo-text"));
    Assertions.assertEquals (0, foundTodoElements.size());
  }

  private WebElement findCheckboxForTodoItemByText(String todoItemText){
    String xpathExpression = String.format("//*[contains(text(),'%s')]/parent::li", todoItemText);
    WebElement liElement = driver.findElement (By.xpath (xpathExpression));
    return liElement.findElement (By.className ("completed-checkbox"));
  }

}
