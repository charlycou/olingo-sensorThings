/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.odata4.client.core.data.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.URI;
import org.apache.olingo.odata4.client.api.data.ServiceDocument;
import org.apache.olingo.odata4.client.core.data.AbstractServiceDocument;
import org.apache.olingo.odata4.client.core.data.XMLServiceDocumentDeserializer;

@JsonDeserialize(using = XMLServiceDocumentDeserializer.class)
public class XMLServiceDocumentImpl extends AbstractServiceDocument implements ServiceDocument {

  private URI baseURI;

  @Override
  public URI getBaseURI() {
    return this.baseURI;
  }

  /**
   * Sets base URI.
   *
   * @param baseURI base URI.
   */
  public void setBaseURI(final URI baseURI) {
    this.baseURI = baseURI;
  }

}
