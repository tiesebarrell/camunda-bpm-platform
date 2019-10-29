/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.xml;

import org.camunda.bpm.engine.BpmnParseException;
import org.camunda.bpm.engine.Problem;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.xml.sax.SAXParseException;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class ProblemImpl implements Problem {

  protected String errorMessage;
  protected String resource;
  protected int line;
  protected int column;
  protected String[] bpmnElementIds;

  public ProblemImpl(SAXParseException e, String resource) {
    concatenateErrorMessages(e);
    this.resource = resource;
    this.line = e.getLineNumber();
    this.column = e.getColumnNumber();
  }

  
  public ProblemImpl(String errorMessage, String resourceName, Element element) {
    this.errorMessage = errorMessage;
    this.resource = resourceName;
    if (element!=null) {
      this.line = element.getLine();
      this.column = element.getColumn();
      String id = element.attribute("id");
      if (id != null && id.length() > 0) {
        this.bpmnElementIds = new String[]{id};
      }
    }
  }

  public ProblemImpl(String errorMessage, String resourceName, Element element, String... bpmnElementIds) {
    this.errorMessage = errorMessage;
    this.resource = resourceName;
    if (element!=null) {
      this.line = element.getLine();
      this.column = element.getColumn();
    }
    this.bpmnElementIds = bpmnElementIds;
  }

  public ProblemImpl(BpmnParseException exception, String resourceName) {
    concatenateErrorMessages(exception);
    this.resource = resourceName;
    Element element = exception.getElement();
    if (element != null) {
      this.line = element.getLine();
      this.column = element.getColumn();
    }
  }

  protected void concatenateErrorMessages(Throwable throwable) {
    while (throwable != null) {
      if (errorMessage == null) {
        errorMessage = throwable.getMessage();
      }
      else {
        errorMessage += ": " + throwable.getMessage();
      }
      throwable = throwable.getCause();
    }
  }

  // getters

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String getResource() {
    return resource;
  }

  @Override
  public int getLine() {
    return line;
  }

  @Override
  public int getColumn() {
    return column;
  }

  @Override
  public String[] getBpmnElementIds() {
    return bpmnElementIds;
  }

  public String toString() {
    return errorMessage+(resource!=null ? " | "+resource : "")+" | line "+line+" | column "+column;
  }
}
