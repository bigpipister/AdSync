@ECHO OFF

set x=%date:~0,4%%date:~5,2%%date:~8,2%

dsquery * -scope subtree OU=MOF,DC=taiwanlife,DC=cht -filter "(&(objectClass=User)(userAccountControl=514)(userParameters=DELETE))" | dsrm -noprompt >> test_%x%.log