import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;




public class FirstClass {
	
	private String username;
	private String buddy;
	private int numMatches;
	
	public FirstClass() {
		username = "";
		buddy = "";
		numMatches = 0;
	}
	
	public void Setup() {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Hello and welcome to the League Buddy Finder.");
		System.out.println("*********************************************");
		System.out.print("Please enter your League of Legends username : ");
		// Add time here to count the amount of time the user is taking to enter their username.
		// If they take too long prompt the user to see if they are still there.
		setUsername(keyboard.nextLine().toLowerCase());
		System.out.print("Please enter your buddy's League of Legends username : ");
		setBuddy(keyboard.nextLine().toLowerCase());
		
		
		setNumMatches(findMatches(usernameSetupForUrl(getUsername()), buddySetupForUrl(getBuddy())));
		
		System.out.println("*********************************************");
		System.out.println("You have played a total of " + getNumMatches() + " matches with " + getBuddy());
	}
	
	public int findMatches(String username, String buddy) {
		WebDriver webdriver = new FirefoxDriver();
		webdriver.get("http://na.op.gg/summoner/userName=" + usernameSetupForUrl(username));
		WebElement renewData = webdriver.findElement(By.className("ladda-button _refreshSummonerInfo summonerRefreshButton"));
		renewData.click();
		WebDriverWait wait = new WebDriverWait(webdriver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noty_topCenter_layout_container")));
		wait = new WebDriverWait(webdriver, 15);
		wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.id("noty_topCenter_layout_container"))));
		
		List<WebElement> LOGames = webdriver.findElements(By.className("GameSimpleStats"));
		List<WebElement> LOPlayers;
		List<WebElement> LOMatches;
		int matchCounter = 0;
		
		for (WebElement game : LOGames) {
			LOPlayers = game.findElements(By.className("summonerName"));
			for (WebElement player : LOPlayers) {
				if (player.findElement(By.tagName("a")).getText().equals(buddy)) {
					matchCounter++;
				}
			}
		}
		return matchCounter;
	}
	
	public String usernameSetupForUrl(String username) {
		username = username.trim();
		if (username.contains(" ")) {
			username = username.replace(' ', '+');
			setUsername(username);
		}
		return username;
	}
	
	public String buddySetupForUrl(String buddy) {
		buddy = buddy.trim();
		if (buddy.contains(" ")) {
			buddy = buddy.replace(' ', '+');
			setBuddy(buddy);
		}
		return buddy;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getBuddy() {
		return buddy;
	}
	
	public int getNumMatches() {
		return numMatches;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setBuddy(String buddy) {
		this.buddy = buddy;
	}
	
	public void setNumMatches(int numMatches) {
		this.numMatches = numMatches;
	}
}
