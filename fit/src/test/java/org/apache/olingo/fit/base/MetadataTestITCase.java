/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.fit.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmAnnotation;
import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.EdmSchema;
import org.apache.olingo.commons.api.edm.EdmTerm;
import org.apache.olingo.commons.api.edm.EdmTypeDefinition;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.TargetType;
import org.apache.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import org.junit.Test;

public class MetadataTestITCase extends AbstractTestITCase {

  @Test
  public void include() {
    final Edm edm = client.getRetrieveRequestFactory().getMetadataRequest(testNorthwindRootURL).execute().getBody();
    assertNotNull(edm);

    final EdmEntityContainer container = edm.getEntityContainer(
        new FullQualifiedName("ODataWebExperimental.Northwind.Model", "NorthwindEntities"));
    assertNotNull(container);

    final EdmEntitySet categories = container.getEntitySet("Categories");
    assertNotNull(categories);
    assertEquals("NorthwindModel", categories.getEntityType().getNamespace());
  }

  @Test
  public void vocabularies() {
    final Edm edm = client.getRetrieveRequestFactory().
        getMetadataRequest(testVocabulariesServiceRootURL).execute().getBody();
    assertNotNull(edm);

    // 1. core
    final EdmSchema core = edm.getSchema("Org.OData.Core.V1");
    assertNotNull(core);
    final EdmSchema coreAlias = edm.getSchema("Core");
    assertEquals(core, coreAlias);

    final EdmTerm descriptionTerm = edm.getTerm(new FullQualifiedName("Core", "Description"));
    assertNotNull(descriptionTerm);
    assertEquals(descriptionTerm.getFullQualifiedName(),
        edm.getTerm(new FullQualifiedName("Org.OData.Core.V1", "Description")).getFullQualifiedName());

    final EdmAnnotation description = core.getAnnotation(descriptionTerm, null);
    assertNotNull(description);
    // assertEquals("Core terms needed to write vocabularies",
    // description.getExpression().asConstant().getValue().asPrimitive().getName());
    assertEquals("Core terms needed to write vocabularies",
        description.getExpression().asConstant().getValueAsString());

    final EdmTerm isLanguageDependent = edm.getTerm(new FullQualifiedName("Core", "IsLanguageDependent"));
    assertNotNull(isLanguageDependent);
    assertTrue(isLanguageDependent.getAppliesTo().contains(TargetType.Property));
    assertTrue(isLanguageDependent.getAppliesTo().contains(TargetType.Term));
    assertEquals(edm.getTypeDefinition(new FullQualifiedName("Core", "Tag")), isLanguageDependent.getType());
    assertEquals(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean),
        ((EdmTypeDefinition) isLanguageDependent.getType()).getUnderlyingType());
    assertNotNull(isLanguageDependent.getAnnotation(descriptionTerm, null));

    final EdmTerm permissions = edm.getTerm(new FullQualifiedName("Core", "Permissions"));
    assertNotNull(permissions);
    assertTrue(permissions.getType() instanceof EdmEnumType);

    // 2. measures
    final EdmSchema measures = edm.getSchema("UoM");
    assertNotNull(measures);

    final EdmTerm scale = edm.getTerm(new FullQualifiedName("UoM", "Scale"));
    assertNotNull(scale);

    final EdmAnnotation requiresTypeInScale =
        scale.getAnnotation(edm.getTerm(new FullQualifiedName("Core", "RequiresType")), null);
    assertNotNull(requiresTypeInScale);
    assertEquals("Edm.Decimal", requiresTypeInScale.getExpression().asConstant().getValueAsString());

    // 3. capabilities
    final EdmTerm deleteRestrictions = edm.getTerm(new FullQualifiedName("Capabilities", "DeleteRestrictions"));
    assertNotNull(deleteRestrictions);
    assertEquals(deleteRestrictions.getType().getFullQualifiedName(),
        edm.getComplexType(new FullQualifiedName("Capabilities", "DeleteRestrictionsType")).getFullQualifiedName());
  }
}
