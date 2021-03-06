package com.pedroalmir.ssnetwork;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.pedroalmir.ssnetwork.controller.HelloAppEngine;

public class HelloAppEngineTest {

  @Test
  public void test() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    new HelloAppEngine().doGet(null, response);
    Assert.assertEquals("text/plain", response.getContentType());
    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
    Assert.assertEquals("Hello App Engine!\r\n", response.getWriterContent().toString());
  }
}
