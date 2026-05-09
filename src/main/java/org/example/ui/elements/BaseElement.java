package org.example.ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import javax.xml.xpath.XPath;

public class BaseElement {
    protected final SelenideElement element;

    public BaseElement(SelenideElement element) {
        this.element = element;
    }

    protected SelenideElement find(By selector) {
        return element.find(selector);
    }

    protected SelenideElement find(String cssCelector) {
        return element.find(cssCelector);
    }

    protected SelenideElement findByXPath(String xPath) {
        return element.find(By.xpath(xPath));
    }

    protected ElementsCollection findAll(By selector) {
        return element.findAll(selector);
    }

    protected ElementsCollection findAll(String cssCelector) {
        return element.findAll(cssCelector);
    }

    protected ElementsCollection findAllByXPath(String xPath) {
        return element.findAll(By.xpath(xPath));
    }
}
