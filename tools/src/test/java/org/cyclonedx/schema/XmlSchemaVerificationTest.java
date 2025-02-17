/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cyclonedx.schema;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class XmlSchemaVerificationTest extends BaseSchemaVerificationTest {

    @TestFactory
    Collection<DynamicTest> dynamicTestsWithCollection() throws Exception {
        final List<String> files = getAllResources();
        final List<DynamicTest> dynamicTests = new ArrayList<>();
        for (final String file: files) {
            if (file.endsWith(".xml")) {
                final CycloneDxSchema.Version schemaVersion;
                if (file.endsWith("-1.0.xml")) {
                    schemaVersion = CycloneDxSchema.Version.VERSION_10;
                } else if (file.endsWith("-1.1.xml")) {
                    schemaVersion = CycloneDxSchema.Version.VERSION_11;
                } else if (file.endsWith("-1.2.xml")) {
                    schemaVersion = CycloneDxSchema.Version.VERSION_12;
                } else if (file.endsWith("-1.3.xml")) {
                    schemaVersion = CycloneDxSchema.Version.VERSION_13;
                } else if (file.endsWith("-1.4.xml")) {
                    schemaVersion = CycloneDxSchema.Version.VERSION_14;
                } else {
                    schemaVersion = null;
                }
                if (file.startsWith("valid") && schemaVersion != null) {
                    dynamicTests.add(DynamicTest.dynamicTest(file, () -> assertTrue(
                            isValid(schemaVersion, "/" + schemaVersion.getVersionString() + "/" + file), file)));
                } else if (file.startsWith("invalid") && schemaVersion != null) {
                    dynamicTests.add(DynamicTest.dynamicTest(file, () -> assertFalse(
                            isValid(schemaVersion, "/" + schemaVersion.getVersionString() + "/" + file), file)));
                }
            }
        }
        return dynamicTests;
    }

    private boolean isValid(CycloneDxSchema.Version version, String resource) throws Exception {
        final File file = new File(this.getClass().getResource(resource).getFile());
        final XmlParser parser = new XmlParser();
        return parser.isValid(file, version);
    }
}
