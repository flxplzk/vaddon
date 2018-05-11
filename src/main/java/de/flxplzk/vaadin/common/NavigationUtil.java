package de.flxplzk.vaadin.common;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * Util class for generating URLs and reading parameter value pairs.
 *
 * @author felix plazek
 */
public class NavigationUtil {

    public static final String VIEW_SEPARATOR = "/";
    public static final String PARAM_SEPARATOR = "&";
    public static final String PARAM_VALUE_SEPARATOR = "=";
    public static final String DEFAULT_VALUE = "";

    public static final String PARAM_SELECTED = "selected";
    public static final String PARAM_WINDOW = "window";
    public static final String PARAM_ID = "_id";


    private NavigationUtil() {
    }

    /**
     * builds a String that fits the Vaadin URI structure containing destination viewName
     * and given parameters.
     *
     * @param state current state returned by the Navigator
     * @param parameterMap Map of attribute value pairs
     * @return destination state
     */
    public static String buildUriWithState(String state, Map<String, String> parameterMap) {
        Optional<String> optionViewName = getViewName(state);
        return buildUriWithViewName(optionViewName.orElse(""), parameterMap);
    }

    public static String buildUriWithViewName(String viewName, Map<String, String> parameterMap) {
        StringBuilder bob = new StringBuilder();
        bob.append(viewName);
        bob.append(VIEW_SEPARATOR);
        for (Map.Entry<String, String> paramPair : parameterMap.entrySet()) {
            if (!(viewName + VIEW_SEPARATOR).equals(bob.toString())) {
                bob.append(PARAM_SEPARATOR);
            }
            bob.append(paramPair.getKey());
            bob.append(PARAM_VALUE_SEPARATOR);
            bob.append(paramPair.getValue());
        }
        return bob.toString();
    }

    /**
     * reads the viewName out of a given NavigationState.
     *
     * @param state any NavigationState
     * @return retrieved ViewName
     */
    public static Optional<String> getViewName(String state) {
        return Optional.of(state.split(VIEW_SEPARATOR)[0]);
    }

    /**
     * reads the value for any given attribute name.
     *
     * @param attribute attribute name
     * @param state Any NavigationState
     * @return Optional.empty() if attribute does not exist;
     *          wanted value wrapped in an Optional otherwise
     */
    public static Optional<String> getPathParamValueFor(final String attribute, final String state) {
        String attributeValue = getAttributeMap(state).getOrDefault(attribute, DEFAULT_VALUE);
        if ("".equals(attributeValue)) {
            return Optional.empty();
        }
        return Optional.of(attributeValue);
    }

    /**
     * reads a attribute value pair map out of any given NavigationState
     * @param state any NavigationState
     * @return returns Map with attribute value pairs.
     *          Empty Map iif no attribute is in the Given NavigationState
     */
    public static Map<String, String> getAttributeMap(final String state) {
        Map<String, String> attributeValueMap = Maps.newHashMap();
        String[] pathParams = getAttributes(state).split(PARAM_SEPARATOR);
        for (String subState : pathParams) {
            String[] valuePair = subState.split(PARAM_VALUE_SEPARATOR);
            if (valuePair.length == 2) {
                attributeValueMap.put(valuePair[0], valuePair[1]);
            }
        }
        return attributeValueMap;
    }

    /**
     * reads AttributeString out of a given NavigationState
     * @param state any NavigationState
     * @return AttributeString
     */
    public static String getAttributes(final String state) {
        String[] args = state.split(VIEW_SEPARATOR);
        if (args.length == 1) {
            return args[0];
        } else {
            return args[1];
        }
    }
}
