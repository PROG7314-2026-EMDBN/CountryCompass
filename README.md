# Country Compass Troubleshooting Challenge

## Overview

Country Compass is an Android application that allows users to search for countries, view details and explore bordering countries.

The application builds and runs, but testing has identified inconsistent behaviour. Your task is to analyse the application, complete the troubleshooting scenario on Arc, and then implement a reliable correction in code.

## Learning outcomes

By completing this activity, you should be able to:
- trace data across a Retrofit service, repository, ViewModel and Compose navigation;
- distinguish between a display value and a stable identifier;
- reproduce an intermittent defect using controlled test cases;
- use evidence to identify a root cause;
- implement and verify a correction without patching individual examples.

## Resources

- CountryCompass Repo -
- The Country Compass H5P activity on Arc
- Android Studio
- An Android emulator or device with internet access

## REST Countries API v5 Setup

Country Compass uses the current REST Countries API v5. You must create a free REST Countries account and use you own API key - DO NOT PAY.

1. Visit `https://restcountries.com` and create an account.
2. Generate or copy an API key from the dashboard.
3. Open `local.properties` in the Android project.
4. Add:

```properties
REST_COUNTRIES_API_KEY=your_api_key_here
```

Do not place the key in a Kotlin file, commit it to Git, include it in screenshots, or submit `local.properties`. The supplied project reads the value through `BuildConfig` and sends it as a bearer token.

## Scenario

During testing, the following inconsistent behaviour was reported:
- A search may return multiple valid countries.
- Selecting one of the displayed countries may open a different country from the one selected.
- Selecting a neighbouring country may fail to load any country information.
- The application does not crash when these issues occur.

Your task is to analyse the application, determine the root cause of the behaviour, and implement an appropriate fix.

Do not begin by rewriting large sections of the application. Reproduce the problem, inspect the data flow, and identify where the selected country loses its intended identity.

## Rules

1. Run and analyse the application before editing it.
2. Do not replace the API with hard-coded data.
3. Do not add special cases for individual countries.
4. Preserve the existing screen structure and architecture.
5. Your correction must work for search results and bordering-country navigation.

## Part 1 - Reproduce and analyse

Do not change the code yet.

1. Open the project in Android Studio and allow Gradle to sync.
2. Run the application.
3. Test several unique country searches.
4. Test searches that may return more than one result.
5. Select different results and compare the selected item with the details shown.
6. Open a country with land borders and select at least one border.
7. Record the observed and expected behaviour.
8. Trace the values passed through the application without implementing a fix.


## Part 2 - Arc Scenario Activity

Complete the **Country Compass: Preserve the Identity** branching scenario on Arc.

The scenario asks you to make troubleshooting decisions using evidence from the application. Complete it individually before modifying the project.

## Part 3 - Correct the code

Return to the Android project and implement a reliable correction.

Your solution must ensure that:
- the country selected in search results is the country opened;
- the application does not depend on result ordering;
- bordering-country selections load the expected country;
- the same approach works for all countries;
- loading and error behaviour remains functional.

Do not submit a correction that only changes the visible search examples.

## Part 4 - Verify

Record at least three tests. Your tests must include:
1. a search that returns a single obvious result;
2. a search that returns multiple or similarly named results;
3. a bordering-country selection.

For each test, record:
- input or selected item;
- expected result;
- actual result after the correction;
- evidence that confirms the correct country was loaded.

## Submission
Submit the corrected Android Studio project;

## Reflection
In no more than 100 words, in the README, explain what you learnt from the activity.