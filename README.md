# Assignment 1: driver-quiz (Due October 9th)

## Members
* [@sirMerr - Tiffany Le-Nguyen](https://github.com/sirMerr)
* [@theoathomas - Theodore Acco-Thomas](https://github.com/theoathomas)

## Project Overview¶

The goal of this assignment is to create a driver's quiz about road signs. You will display 4 road sign images and a description that matches one sign. The user is to click on the image that matches the description. They are to be given 2 tries. If the first selection is incorrect the image is to be replaced with a "wrong" image. If the first selection is correct or the second incorrect make the next button visible. You will keep counters for correct / incorrect answers across activities. You will also keep a counter of quiz attempts, all three counters will be maintained after the app is killed.

## Sample UIs
| Default UI  (main layout) | Clicked image incorrect: |
| --- | --- |
|<img src="https://cdn.discordapp.com/attachments/307959450805862400/363385789859627008/Screen_Shot_2017-09-29_at_2.08.06_PM.png"/>|<img src="https://cdn.discordapp.com/attachments/307959450805862400/363385794330755073/Screen_Shot_2017-09-29_at_2.08.17_PM.png"> |

## Specifications¶

- [X] App name in the Launcher must be DQ followed by your first names: `DQName1Name2`
- [X] Create and use your own icon
- [X] Show the user a minimum of 4 questions, one at a time for the quiz. You will need a least 15 images so the answers are not obvious. One text question per image. One hint per image Randomly select the image/question.
- [ ] Maintain all counters between run times (SharedPreferences.)
- [X] Maintain state in the Bundleas needed for display information
- [X] You are responsible for the logic to select a random image and it's text for each question. Randomly select 3 other images to display with it. Use them to populate your layout so they are displayed in a random order using `setBackground()` or `setImageResource()` Keep track of the correct answer. Hint: Resource class, Random class, Array class, Collection class…
- UI must show
  - [X] 4 image buttons
  - [X] question count ex: question 1 of 4
  - [X] counts for correct and incorrect: correct 1 incorrect 0
  - [X] hint button
  - [X] about button
  - [X] next button (hidden for each new question)
- [X] Make sure the images scale when the screen is rotated and you can see the whole image (if resolution is off, that is ok, you only need one set of images but they must look centred and fill the view, no gaps unless part of the image) You may need to crop so they are similar sizes or convert to 9 patch images.

  Here are some cheat sheets for views and layouts See also ScaleType Android & this is pretty good a visual guide Here are some description and image sources:
  * http://www.drivingtest.ca/quebec-driving-test/
  * https://testdeconnaissances.saaq.gouv.qc.ca/en/

- [X] Create and use a unique Icon: http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
- [ ] When the last question is answered, update the saved scores (keep the last two scores) (SharedPreferences)
- [ ] If the app ends before they finish the questions (n.b. override finish) When the app is started again use shared preferences to return the user to next question and set the counters. You need counters to implement that, along with an array of what questions have been answered.

- [ ] Image Buttons actions:
  - [X] If it is the correct image:
    - [ ] add one to the correct count, update score, update question counter
    - [X] make all images not clickable
    - [X] change the image in some way to show it is the correct answer (you may want to try and/or an alternate image that shows the same with a border) see: http://developer.android.com/guide/topics/ui/controls/button.html
    - [X] enable and make visible the next button
  - [X] if it is not the correct image (first incorrect):
    - [X] set it to not clickable
    - [X] change the image in some way to show it is incorrect
  - [X] if it is not the correct image (second incorrect):
    - [X] add one to the incorrect count, update score, update question counter
    - [X] set all images to not clickable
    - [X] highlight the correct image in some way (see point a. iii. )
    - [X] enable and make the next button visible

- [ ] About button: fire a new Activity:
  - [X] Create a layout ( UI ) with
  - [X] A short paragraph describing how to play
  - [X] Credits (who wrote the app)
  - [ ] Any saved past scores (SharedPreferences)
  - [X] Any small image you like (ex: your photos)
- [X] Hint button: fire an Intent to do a google search using the current question's description text, maybe prefix it with "road sign" See Perform a Web Search here https://developer.android.com/guide/components/intents-common.html#Search
- [X] Be sure to comment your methods and all code thoroughly, use Javadocs for class and your methods.
- [X] Use Logging
- [X] All of your UI strings must be in strings.xml.
- [X] Once you have tested and the UI and logic are operating correctly, use localization to make your app bilingual (add french) strings http://developer.android.com/guide/topics/resources/localization.html

## Collaboration (you may NOT do this solo)¶

1. You will do this assignment with a teacher assigned partner.
2. You will be given access to an assigned a repo by the teacher and expected to use the protected workflow from git lab 3 READ the workflow overview here You will have to create a branch from the protected branch master and discuss and agree on merging pull requests. Most expectations are in the workflow overview but see also collaboration expectations.
3. Due in two weeks, I will clone master once the deadline expires, ensure all of your changes are applied to it via a merged pull request.
4. See collaboration expectations for rules & regs

## Learning Objectives¶

* UI layouts, widgets
* State, SharedPreferences
* Activity lifecycle
* Drawable resources
* String resources
* Explicit and Implicit Intents
* Localisation
* Collaboration using git
* UI Example with minimal xml¶

## UI Example  with minimal xml

Below is my layout overview, just to give you an idea. You are free to use what you wish [layout optimisation](https://developer.android.com/training/improving-layouts/optimizing-layout.html),   [RelativeLayout](https://developer.android.com/guide/topics/ui/layout/relative.html), [Constraint](https://developer.android.com/training/constraint-layout/index.html) [Layout](https://developer.android.com/training/constraint-layout/index.html) etc.  Just be sure it resizes on rotate.  (Try to make yours prettier than mine :) )

Layout:
<img src="https://cdn.discordapp.com/attachments/307959450805862400/363385796457398283/Screen_Shot_2017-09-29_at_2.08.26_PM.png"/>
XML:
```
<LinearLayout >  (vertical)
    <LinearLayout> (horizontal)
        <TextView />
        <TextView />
    </LinearLayout>
    <LinearLayout > (horizontal)
      <ImageButton/>
      <ImageButton/>
    </LinearLayout>
    <LinearLayout> (horizontal)
        <ImageButton />
        <ImageButton />
    </LinearLayout>
    <TextView/>
    <TextView/>
    <LinearLayout> (horizontal) 
        <Button />
        <Button />
    </LinearLayout>
    <Button/>
</LinearLayout>

```
