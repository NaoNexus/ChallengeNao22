<?xml version="1.0" encoding="UTF-8" ?>
<Package name="4_ex_dialogo" format_version="5">
    <Manifest src="manifest.xml" />
    <BehaviorDescriptions>
        <BehaviorDescription name="behavior" src="behavior_1" xar="behavior.xar" />
    </BehaviorDescriptions>
    <Dialogs>
        <Dialog name="dialogo_di_prova" src="dialogo_di_prova/dialogo_di_prova.dlg" />
    </Dialogs>
    <Resources>
        <File name="mikhael-landscape-paisaje" src="behavior_1/sounds/mikhael-landscape-paisaje.ogg" />
    </Resources>
    <Topics>
        <Topic name="dialogo_di_prova_iti" src="dialogo_di_prova/dialogo_di_prova_iti.top" topicName="dialogo_di_prova" language="it_IT" nuance="iti" />
    </Topics>
    <IgnoredPaths />
    <Translations auto-fill="it_IT">
        <Translation name="translation_en_US" src="translations/translation_en_US.ts" language="en_US" />
        <Translation name="translation_it_IT" src="translations/translation_it_IT.ts" language="it_IT" />
    </Translations>
</Package>
