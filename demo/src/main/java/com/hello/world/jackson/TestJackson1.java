package com.hello.world.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.TestCase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestJackson1 extends TestCase {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 忽略未知的JSON字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * str to obj
     */
    public void test1() throws IOException {
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5, \"doors2\":5 }";

        Car car = mapper.readValue(carJson, Car.class);

//        Car xxx = mapper.readValue(new File("xxx"), Car.class);

        System.out.println(car);
    }

    /**
     * str to obj[]
     */
    public void test2() throws JsonProcessingException {
        String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

        Car[] cars2 = mapper.readValue(jsonArray, Car[].class);

        System.out.println(Arrays.toString(cars2));
    }

    /**
     * str to list
     */
    public void test3() throws JsonProcessingException {
        String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

        List<Car> cars = mapper.readValue(jsonArray, new TypeReference<List<Car>>() {
        });
        System.out.println(cars);
    }

    /**
     * str to map
     */
    public void test4() throws JsonProcessingException {
        String jsonObject = "{\"brand\":\"ford\", \"doors\":5, \"doors2\":5}";

        Map<String, Object> map = mapper.readValue(jsonObject, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map);
    }

    /**
     * obj to str
     */
    public void test5() throws JsonProcessingException {
        Car car = new Car();
        car.setBrand("宝马");
        car.setDoors(4);

        String str = mapper.writeValueAsString(car);
        System.out.println(str);
    }

    /**
     * 时间格式化处理
     */
    public void test6() throws JsonProcessingException {
        Transaction transaction = new Transaction("transfer", new Date());

        // 默认处理为毫秒
        String str = mapper.writeValueAsString(transaction);
        System.out.println(str);

        // 格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFormat);
        String str2 = mapper.writeValueAsString(transaction);
        System.out.println(str2);
    }

    /**
     * jsonNode
     */
    public void test7() throws JsonProcessingException {
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        JsonNode jsonNode = mapper.readValue(carJson, JsonNode.class);
        System.out.println(jsonNode);

        JsonNode jsonNode1 = mapper.readTree(carJson);
        System.out.println(jsonNode1);

    }

    /**
     *
     */
    public void test8() throws JsonProcessingException {

        String carJson =
                "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
                        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
                        "  \"nestedObject\" : { \"field\" : \"value\" } }";

        JsonNode jsonNode = mapper.readValue(carJson, JsonNode.class);

        JsonNode brandNode = jsonNode.get("brand");
        String brand = brandNode.asText();
        System.out.println("brand = " + brand);

        JsonNode doorsNode = jsonNode.get("doors");
        int doors = doorsNode.asInt();
        System.out.println("doors = " + doors);

        JsonNode array = jsonNode.get("owners");
        JsonNode myJsonNode = array.get(0);
        String john = myJsonNode.asText();
        System.out.println("john  = " + john);

        JsonNode child = jsonNode.get("nestedObject");
        JsonNode childField = child.get("field");
        String field = childField.asText();
        System.out.println("field = " + field);
    }

    /**
     * obj to jsonNode
     */
    public void test9() {
        Car car = new Car();
        car.setBrand("Cadillac");
        car.setDoors(4);

        JsonNode carJsonNode = mapper.valueToTree(car);
        System.out.println(carJsonNode);
    }

    /**
     * jsonNode to obj
     */
    public void test10() throws JsonProcessingException {

        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

        JsonNode carJsonNode = mapper.readTree(carJson);

        Car car = mapper.treeToValue(carJsonNode, Car.class);
        System.out.println(car);
    }

    /**
     *
     */
    public void test11() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode parentNode = objectMapper.createObjectNode();

        JsonNode childNode = null; // readJsonIntoJsonNode();

        parentNode.set("child1", childNode);
    }

    public void test12(){
        /*
@JsonProperty
用于属性，把属性的名称序列化时转换为另外一个名称。示例：@JsonProperty(“birth_ date”)private Date birthDate;

@JsonFormat
用于属性或者方法，把属性的格式序列化时转换成指定的格式。示例： @JsonFormat(timezone = “GMT+8”, pattern = “yyyyMM-dd HH:mm”) public DategetBirthDate()

@JsonPropertyOrder
用于类， 指定属性在序列化时 json 中的顺序 ， 示例：@JsonPropertyOrder({ “birth_Date”, “name” })public class Person

@JsonCreator
用于构造方法，和 @JsonProperty 配合使用，适用有参数的构造方法。 示例： @JsonCreator public Person(@JsonProperty(“name”)String name) {…}

@JsonAnySetter
用于属性或者方法，设置未反序列化的属性名和值作为键值存储到 map 中 @JsonAnySetter public void set(String key, Object value) { map.put(key, value); }

@JsonAnyGetter
用于方法 ，获取所有未序列化的属性public Map<String, Object> any() { return map; } ;

         */
    }

}
