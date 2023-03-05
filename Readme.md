# MODULE 1: Learn Spring Security Core - Secure a ﻿﻿Simple﻿﻿ Spring MVC Application
This is the codebase for Module 'Secure a Simple Spring MVC Application' of [Learn Spring Security Core](https://bit.ly/github-lssc)

## Non-Standard Modules

_m1-lesson6-noboot_ 
- is the version of _m1-lesson6_ that  doesn't use Spring Boot


# MODULE 2: Learn Spring Security Core - A Full Registration Flow
This is the codebase for Module 'A Full Registration Flow' of [Learn Spring Security Core](https://bit.ly/github-lssc)

## Notes Regarding the SMTP/Email Configuration
Note that even though the email sending logic is configured in the codebase, there is additional SMTP configuration required:

1. Define the `spring.mail.username` and the `spring.mail.password` application properties

2. If you're using Gmail - Google's account security configurations:
    1. Enable 2FA (following these steps: https://support.google.com/accounts/answer/185839).
    2. Add an App Password to your account (following these steps: https://support.google.com/accounts/answer/185833) and using that code as the password in the code.
