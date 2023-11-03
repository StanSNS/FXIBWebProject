package fxibBackend.entity.enums;

import fxibBackend.exception.ResourceNotFoundException;

/**
 * Enumeration representing various topics.

 * This enum defines a set of topics commonly discussed in forex trading communities.
 * Each topic is associated with a descriptive text.
 */
public enum TopicEnum {
    FOREX_BASICS("Forex Basics"),
    TRADING_STRATEGIES("Trading Strategies"),
    TECHNICAL_ANALYSIS("Technical Analysis"),
    FUNDAMENTAL_ANALYSIS("Fundamental Analysis"),
    RISK_MANAGEMENT("Risk Management"),
    BROKER_RECOMMENDATIONS("Broker Recommendations"),
    TRADING_TOOLS("Trading Tools"),
    TRADING_SOFTWARE("Trading Software"),
    EDUCATION_AND_LEARNING("Education and Learning"),
    NEWS_AND_EVENTS("News and Events"),
    SCAMS_AND_FRAUDS("Scams and Frauds"),
    TRADING_JOURNALS("Trading Journals"),
    TIPS_AND_TRICKS("Tips and Tricks"),
    FOREX_AND_TAXES("Forex and Taxes"),
    RETIREMENT_PLANNING("Retirement Planning"),
    TRADING_FOR_A_LIVING("Trading for a Living"),
    TRADING_AND_PSYCHOLOGY("Trading and Psychology"),
    TRADING_BOOKS("Trading Books"),
    TRADING_AUTOMATION("Trading Automation"),
    OTHER("Other");

    private final String text;

    TopicEnum(String text) {
        this.text = text;
    }

    /**
     * Get the descriptive text associated with the topic.
     *
     * @return The descriptive text of the topic.
     */
    public String getText() {
        return text;
    }

    /**
     * Retrieve the enum value based on its descriptive text.
     *
     * @param text The descriptive text of the topic.
     * @return The corresponding TopicEnum value.
     * @throws ResourceNotFoundException if the provided text does not match any topic.
     */
    public static TopicEnum getFromText(String text) {
        for (TopicEnum topicEnum : TopicEnum.values()) {
            if (topicEnum.getText().equalsIgnoreCase(text)) {
                return topicEnum;
            }
        }
        throw new ResourceNotFoundException();
    }

}
