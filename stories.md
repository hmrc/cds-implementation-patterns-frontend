# FizzBuzz Service

FizzBuzz is a brilliant GOV.UK service which, given any two positive integers, can tell you whether their product is a
"fizz", "buzz", or "fizzbuzz". Because this is the government we're talking about, there are naturally some outrageous
business rules to do with whether or not you are asking on a bank holiday just to ensure that, ultimately, the complete
service cannot be implemented as a simple calculator function.

## Story 1: basic user input

The user enters 2 numbers. They are then informed whether the product of these two numbers is "fizz", "buzz", or "fizzbuzz".

* "fizz" should be returned if the product of the two user-supplied numbers is a multiple of 3
* "buzz" should be returned if the product of the two user-supplied numbers is a multiple of 5
* "fizzbuzz" should be returned if the product of the two user-supplied numbers is a multiple of 15
* In all other cases, "none" should be returned

## Story 2: basic user input validation

For numbers input by the user, the application will reject any values that are less than 1. They will informed of the
error via an appropriately clear message in English and permitted to modify their input. 

## Story 3: business rules

If the product of the user supplied numbers contains the digit "3" *and* the user is utilising the service on a bank
holiday, then "lucky" should be returned.

## Story 4: authentication

Only users who have a "FIZZ-BUZZ" enrolment on MDTP should be permitted to use the service.

