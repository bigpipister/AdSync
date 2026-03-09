-- 外:人員新增 (TAX)
select metaversep0_.objectguid           ,
       metaversep0_.placeholder          ,
       metaversep0_.accountexpires       ,
       metaversep0_.cn                   ,
       metaversep0_.department           ,
       metaversep0_.displayname          ,
       metaversep0_.dn                   ,
       metaversep0_.dn_hash              ,
       metaversep0_.employeeid           ,
       metaversep0_.extensionattribute1  ,
       metaversep0_.extensionattribute10 ,
       metaversep0_.extensionattribute11 ,
       metaversep0_.extensionattribute12 ,
       metaversep0_.extensionattribute13 ,
       metaversep0_.extensionattribute14 ,
       metaversep0_.extensionattribute15 ,
       metaversep0_.extensionattribute2  ,
       metaversep0_.mail                 ,
       metaversep0_.memberof             ,
       metaversep0_.ou                   ,
       metaversep0_.pwdlastset           ,
       metaversep0_.samaccountname       ,
       metaversep0_.sn                   ,
       metaversep0_.syncattrs_hash       ,
       metaversep0_.title                ,
       metaversep0_.unicodepwd           ,
       metaversep0_.unicodepwd_hash      ,
       metaversep0_.useraccountcontrol   ,
       metaversep0_.userparameters       ,
       metaversep0_.userprincipalname    ,
       metaversep0_.whenchanged          ,
       metaversep0_.whencreated
from metaverse_person_details_external metaversep0_
where not (exists(select (connectors1_.objectguid, connectors1_.placeholder)
                  from connector_space_ad_person_details connectors1_
                  where metaversep0_.cn = connectors1_.cn
                    and connectors1_.placeholder = 'external'))
  and metaversep0_.placeholder = 'external'
  and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=taiwanlife1,DC=cht%')))
  and metaversep0_.extensionattribute1 = '1'
order by metaversep0_.whencreated asc

-- 外:組織新增 (TAX)
select metaverseo0_.objectguid     ,
       metaverseo0_.placeholder    ,
       metaverseo0_.displayname    ,
       metaverseo0_.dn             ,
       metaverseo0_.dn_hash        ,
       metaverseo0_.ou             ,
       metaverseo0_.syncattrs_hash ,
       metaverseo0_.whenchanged    ,
       metaverseo0_.whencreated    
from metaverse_organizationalunit_details_external metaverseo0_
where not (exists(select (connectors1_.objectguid, connectors1_.placeholder)
                  from connector_space_ad_organizationalunit_details connectors1_
                  where metaverseo0_.ou = connectors1_.ou
                    and connectors1_.placeholder = 'external'))
  and (lower(metaverseo0_.dn) like lower(('%OU=MOF,DC=taiwanlife1,DC=cht%')))
order by metaverseo0_.whencreated asc, length(metaverseo0_.dn) asc

-- 外:群組新增 (TAX)
select metaverseg0_.dn,
       metaverseg0_.placeholder,
       metaverseg0_.cn,
       metaverseg0_.dn_hash,
       metaverseg0_.extensionattribute10,
       metaverseg0_.extensionattribute11,
       metaverseg0_.extensionattribute12,
       metaverseg0_.extensionattribute13,
       metaverseg0_.extensionattribute14,
       metaverseg0_.extensionattribute15,
       metaverseg0_.grouptype,
       metaverseg0_.mail_enabled,
       metaverseg0_.memberof,
       metaverseg0_.name,
       metaverseg0_.objectguid,
       metaverseg0_.samaccountname,
       metaverseg0_.syncattrs_hash,
       metaverseg0_.whenchanged,
       metaverseg0_.whencreated
from metaverse_group_details_external metaverseg0_
where not (exists(select (connectors1_.dn, connectors1_.placeholder)
                  from connector_space_ad_group_details connectors1_
                  where metaverseg0_.cn = connectors1_.cn
                    and connectors1_.placeholder = 'external'))
  and (lower(metaverseg0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by metaverseg0_.whencreated asc, length(metaverseg0_.dn) asc
