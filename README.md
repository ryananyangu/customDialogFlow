# Custom Dilogue flow Application

[![codecov](https://codecov.io/gh/ryananyangu/customDialogFlow/branch/master/graph/badge.svg)](https://codecov.io/gh/ryananyangu/customDialogFlow)
[![Code Grade](https://www.code-inspector.com/project/4161/status/svg)](https://frontend.code-inspector.com/public/project/4161/customDialogFlow/dashboard)
[![Code Grade](https://www.code-inspector.com/project/4161/score/svg)](https://frontend.code-inspector.com/public/project/4161/customDialogFlow/dashboard)


## Refactoring 
1. Models
    User --> System Users 
    - Organization = String
    - Active = Boolean
    - First Name = String 
    - Last Name = String
    - Roles = list[SPRING INTERNAL]
    - Email Address = String 
    - Date Created = DateTimeOnCreation
    - Last Updated = DateTimeLastModified

    Journey --> Exact incomming message and System generated response to the question
    - Incoming request = String
    - Response Text = String

    ArchiveSession --> Collection of sessions and journey
    - SessionID = String 
    - Dialing number = String
    - ServiceCode = String
    - Status = String enum (FAILED,INCOMPLETE,COMPLETE)
    - Datecreated = DateTimeOnCreation
    - DateUpdated = DateTimeCurrent
    - SessionMessages = list[Sessions] 

    Session --> Current active session
    - Dialling Number **
    - ExtraData
    - Journey = 
    - SessionID
    - Datecreated = DateTimeOnCreation
    - DateUpdated = DateTimeCurrent

    Menu --> collection of screens
    - ShortCode = String
    - Screens = list[Screen]
    - Organization = String
    - Datecreated = DateTimeOnCreation
    - DateUpdated = DateTimeCurrent

    Screen --> User defined message meta data
    - NodeName - String
    - isScreenActive - Boolean
    - nodeExtraData - HashMap
    - nodeItems - List[HashMaps]
    - nodeOptions - List[Strings]
    - screenMext - String
    - ScreenType - enum->Strings(raw_input,items,options)
    - ScreenText - String

    Organization --> Name
    - Organization name 
    - Datecreated = DateTimeOnCreation
    - DateUpdated = DateTimeCurrent
    - Contact
    ShortCode --> auto
    - serviceCode - TypesOfServiceCode
    - Provider (MessageBird/AT/Saf/Airtel)
    - Status - Active/Disable 
    - Organization -

    TypesOfServiceCode
    - Name - ID
    - Description

    applicationVars
    - Key - ID
    - Value 
    - Environment (prod,testbed,preprod)


Usage of enums