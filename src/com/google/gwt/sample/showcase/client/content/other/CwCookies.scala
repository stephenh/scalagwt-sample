/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.showcase.client.content.other

import com.google.gwt.core.client.GWT
import com.google.gwt.core.client.RunAsyncCallback
import com.google.gwt.event.dom.client.ChangeEvent
import com.google.gwt.event.dom.client.ChangeHandler
import com.google.gwt.event.dom.client.ClickEvent
import com.google.gwt.event.dom.client.ClickHandler
import com.google.gwt.i18n.client.Constants
import com.google.gwt.sample.showcase.client.ContentWidget
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseData
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseSource
import com.google.gwt.user.client.Command
import com.google.gwt.user.client.Cookies
import com.google.gwt.user.client.DeferredCommand
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.rpc.AsyncCallback
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.Grid
import com.google.gwt.user.client.ui.ListBox
import com.google.gwt.user.client.ui.TextBox
import com.google.gwt.user.client.ui.Widget

import java.util.Collection
import java.util.Date

import scala.collection.JavaConversions._

object CwCookies {
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  trait CwConstants extends Constants with ContentWidget.CwConstants {
    def cwCookiesDeleteCookie(): String

    def cwCookiesDescription(): String

    def cwCookiesExistingLabel(): String

    def cwCookiesInvalidCookie(): String

    def cwCookiesName(): String

    def cwCookiesNameLabel(): String

    def cwCookiesSetCookie(): String

    def cwCookiesValueLabel(): String
  }
}

/**
 * Example file.
 */
class CwCookies(constants: CwCookies.CwConstants) extends ContentWidget(constants) {

  /**
   * The timeout before a cookie expires, in milliseconds. Current one day.
   */
  @ShowcaseData
  private val COOKIE_TIMEOUT = 1000 * 60 * 60 * 24

  /**
   * A {@link TextBox} that holds the name of the cookie.
   */
  @ShowcaseData
  private var cookieNameBox: TextBox = null

  /**
   * A {@link TextBox} that holds the value of the cookie.
   */
  @ShowcaseData
  private var cookieValueBox: TextBox = null

  /**
   * The {@link ListBox} containing existing cookies.
   */
  @ShowcaseData
  private var existingCookiesBox: ListBox = null

  override def getDescription() = constants.cwCookiesDescription

  override def getName() = constants.cwCookiesName

  override def hasStyle() = false

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  override def onInitialize(): Widget = {
    // Create the panel used to layout the content
    val mainLayout = new Grid(3, 3)

    // Display the existing cookies
    existingCookiesBox = new ListBox()
    val deleteCookieButton = new Button(constants.cwCookiesDeleteCookie)
    deleteCookieButton.addStyleName("sc-FixedWidthButton")
    mainLayout.setHTML(0, 0, "<b>" + constants.cwCookiesExistingLabel + "</b>")
    mainLayout.setWidget(0, 1, existingCookiesBox)
    mainLayout.setWidget(0, 2, deleteCookieButton)

    // Display the name of the cookie
    cookieNameBox = new TextBox()
    mainLayout.setHTML(1, 0, "<b>" + constants.cwCookiesNameLabel + "</b>")
    mainLayout.setWidget(1, 1, cookieNameBox)

    // Display the name of the cookie
    cookieValueBox = new TextBox()
    val setCookieButton = new Button(constants.cwCookiesSetCookie)
    setCookieButton.addStyleName("sc-FixedWidthButton")
    mainLayout.setHTML(2, 0, "<b>" + constants.cwCookiesValueLabel + "</b>")
    mainLayout.setWidget(2, 1, cookieValueBox)
    mainLayout.setWidget(2, 2, setCookieButton)

    // Add a handler to set the cookie value
    setCookieButton.addClickHandler(new ClickHandler() {
      def onClick(event: ClickEvent) {
        val name = cookieNameBox.getText()
        val value = cookieValueBox.getText()
        val expires = new Date((new Date()).getTime() + COOKIE_TIMEOUT)

        // Verify the name is valid
        if (name.length() < 1) {
          Window.alert(constants.cwCookiesInvalidCookie())
          return
        }

        // Set the cookie value
        Cookies.setCookie(name, value, expires)
        refreshExistingCookies(name)
      }
    })

    // Add a handler to select an existing cookie
    existingCookiesBox.addChangeHandler(new ChangeHandler() {
      def onChange(event: ChangeEvent) = updateExstingCookie()
    })

    // Add a handler to delete an existing cookie
    deleteCookieButton.addClickHandler(new ClickHandler() {
      def onClick(event: ClickEvent) {
        val selectedIndex = existingCookiesBox.getSelectedIndex()
        if (selectedIndex > -1
            && selectedIndex < existingCookiesBox.getItemCount()) {
          val cookieName = existingCookiesBox.getValue(selectedIndex)
          Cookies.removeCookie(cookieName)
          existingCookiesBox.removeItem(selectedIndex)
          updateExstingCookie()
        }
      }
    })

    // Return the main layout
    refreshExistingCookies(null)
    return mainLayout
  }
  
  override def asyncOnInitialize(callback:AsyncCallback[Widget]) {
    GWT.runAsync(new RunAsyncCallback() {
      def onFailure(caught: Throwable) = callback.onFailure(caught)

      def onSuccess() = callback.onSuccess(onInitialize())
    })
  }

  /**
   * Refresh the list of existing cookies.
   * 
   * @param selectedCookie the cookie to select by default
   */
  @ShowcaseSource
  private def refreshExistingCookies(selectedCookie: String) {
    // Clear the existing cookies
    existingCookiesBox.clear()

    // Add the cookies
    var selectedIndex = 0
    val cookies = Cookies.getCookieNames()
    for (cookie <- cookies) {
      existingCookiesBox.addItem(cookie)
      if (cookie.equals(selectedCookie)) {
        selectedIndex = existingCookiesBox.getItemCount() - 1
      }
    }

    // Select the index of the selectedCookie. Use a DeferredCommand to give
    // the options time to register in Opera.
    val selectedIndexFinal = selectedIndex
    DeferredCommand.addCommand(new Command() {
      def execute() {
        // Select the default cookie
        if (selectedIndexFinal < existingCookiesBox.getItemCount()) {
          existingCookiesBox.setSelectedIndex(selectedIndexFinal)
        }

        // Display the selected cookie value
        updateExstingCookie()
      }
    })
  }

  /**
   * Retrieve the value of the existing cookie and put it into to value label.
   */
  @ShowcaseSource
  private def updateExstingCookie() {
    // Cannot update if there are no items
    if (existingCookiesBox.getItemCount() < 1) {
      cookieNameBox.setText("")
      cookieValueBox.setText("")
      return
    }

    val selectedIndex = existingCookiesBox.getSelectedIndex()
    val cookieName = existingCookiesBox.getValue(selectedIndex)
    val cookieValue = Cookies.getCookie(cookieName)
    cookieNameBox.setText(cookieName)
    cookieValueBox.setText(cookieValue)
  }
}
