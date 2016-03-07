package net.kyouko.cloudier.api;

/**
 * Class for request parameters specifying result in JSON format.
 *
 * @author beta
 */
public class JsonParams extends Params {

    public JsonParams() {
        put("format", "json");
    }

}
