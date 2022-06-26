package org.erick.service;

import org.erick.domain.driver.DriverCache;
import org.erick.domain.passenger.PassengerCache;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { DriverCache.class, PassengerCache.class }, schemaPackageName = "org.erick.domain")
public interface LibraryInitalizer extends SerializationContextInitializer {

}