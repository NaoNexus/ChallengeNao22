<?xml version="1.0" encoding="UTF-8" ?>
<Package name="NAOProject" format_version="5">
    <Manifest src="manifest.xml" />
    <BehaviorDescriptions>
        <BehaviorDescription name="behavior" src="behavior_1" xar="behavior.xar" />
    </BehaviorDescriptions>
    <Dialogs>
        <Dialog name="AskRestart" src="behavior_1/AskRestart/AskRestart.dlg" />
    </Dialogs>
    <Resources>
        <File name="icon" src="behavior_1/icon.png" />
        <File name="choice_sentences" src="behavior_1/Aldebaran/choice_sentences.xml" />
        <File name="angry6" src="behavior_1/sounds/angry6.wav" />
        <File name="eto9" src="behavior_1/sounds/eto9.wav" />
        <File name="interested4" src="behavior_1/sounds/interested4.wav" />
        <File name="popup" src="behavior_1/sounds/popup.ogg" />
        <File name="mikhael-landscape-paisaje" src="behavior_1/sounds/mikhael-landscape-paisaje.ogg" />
    </Resources>
    <Topics>
        <Topic name="AskRestart_enu" src="behavior_1/AskRestart/AskRestart_enu.top" topicName="AskRestart" language="en_US" nuance="enu" />
        <Topic name="AskRestart_frf" src="behavior_1/AskRestart/AskRestart_frf.top" topicName="AskRestart" language="fr_FR" nuance="frf" />
        <Topic name="AskRestart_mnc" src="behavior_1/AskRestart/AskRestart_mnc.top" topicName="AskRestart" language="zh_CN" nuance="mnc" />
    </Topics>
    <IgnoredPaths />
    <Translations>
        <Translation name="translation_en_US" src="translations/translation_en_US.ts" language="en_US" />
    </Translations>
</Package>
