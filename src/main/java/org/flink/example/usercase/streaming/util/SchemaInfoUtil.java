package org.flink.example.usercase.streaming.util;

import org.apache.avro.Schema;

public class SchemaInfoUtil {
    public static String[] getfield(String nameAndType) {
        String[] nt = nameAndType.split("\\:");
        return nt;
    }

    public static String getfieldBy(String fieldName, String fieldType) {
        return String.format("{\"name\":\"%s\",\"type\":[\"%s\",\"null\"]}", fieldName, fieldType);
    }

    public static String avroSchemaInfo() {
        String head = "\"type\":\"record\"," + "\"name\":\"sdk\",";
        String fields = String.format("\"fields\":[%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s]",
                getfield("trace_id:string"), getfield("user_id:string"));
        return String.format("{%s" + "%s}",head,fields);
    }

    public static Schema getSchema(String metaSchema) {
        Schema parser = Schema.parse(metaSchema);
        Schema schema = parser.parse(metaSchema);
        return schema;
    }
}
