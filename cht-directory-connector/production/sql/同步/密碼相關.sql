-- 查詢密碼異動

select metaversep0_.objectguid           ,
       connectors1_.objectguid           ,
       connectors1_.placeholder          ,
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
       metaversep0_.whencreated          ,
       connectors1_.accountexpires       ,
       connectors1_.cn                   ,
       connectors1_.department           ,
       connectors1_.displayname          ,
       connectors1_.dn                   ,
       connectors1_.dn_hash              ,
       connectors1_.employeeid           ,
       connectors1_.extensionattribute1  ,
       connectors1_.extensionattribute10 ,
       connectors1_.extensionattribute11 ,
       connectors1_.extensionattribute12 ,
       connectors1_.extensionattribute13 ,
       connectors1_.extensionattribute14 ,
       connectors1_.extensionattribute15 ,
       connectors1_.extensionattribute2  ,
       connectors1_.mail                 ,
       connectors1_.memberof             ,
       connectors1_.objectcategory       ,
       connectors1_.objectclass          ,
       connectors1_.ou                   ,
       connectors1_.pwdlastset           ,
       connectors1_.samaccountname       ,
       connectors1_.sn                   ,
       connectors1_.syncattrs_hash       ,
       connectors1_.title                ,
       connectors1_.unicodepwd           ,
       connectors1_.unicodepwd_hash      ,
       connectors1_.useraccountcontrol   ,
       connectors1_.userparameters       ,
       connectors1_.userprincipalname    ,
       connectors1_.whenchanged          ,
       connectors1_.whencreated          
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash or metaversep0_.unicodepwd_hash is null or
       connectors1_.unicodepwd_hash is null)
  and connectors1_.placeholder='inner'
order by metaversep0_.whencreated asc

select
       connectors1_.placeholder,
       metaversep0_.accountexpires,
       metaversep0_.cn,
       metaversep0_.department,
       metaversep0_.displayname,
       metaversep0_.dn,
       metaversep0_.employeeid,
       metaversep0_.ou,
       metaversep0_.pwdlastset,
       extract(epoch from metaversep0_.pwdlastset),
       metaversep0_.samaccountname,
       metaversep0_.sn,
       metaversep0_.title,
       metaversep0_.unicodepwd,
       metaversep0_.unicodepwd_hash,
       metaversep0_.useraccountcontrol,
       metaversep0_.userparameters,
       metaversep0_.userprincipalname,
       connectors1_.accountexpires,
       connectors1_.cn,
       connectors1_.department,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.employeeid,
       connectors1_.ou,
       connectors1_.pwdlastset,
       extract(epoch from connectors1_.pwdlastset),
       connectors1_.samaccountname,
       connectors1_.sn,
       connectors1_.syncattrs_hash,
       connectors1_.title,
       connectors1_.unicodepwd,
       connectors1_.unicodepwd_hash,
       connectors1_.useraccountcontrol,
       connectors1_.userparameters,
       connectors1_.userprincipalname
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash or metaversep0_.unicodepwd_hash is null or
       connectors1_.unicodepwd_hash is null)
  and connectors1_.placeholder = 'inner'
order by metaversep0_.whencreated asc

-- eDir 密碼需同步至 AD

select
       connectors1_.placeholder,
       metaversep0_.accountexpires,
       metaversep0_.cn,
       metaversep0_.department,
       metaversep0_.displayname,
       metaversep0_.dn,
       metaversep0_.employeeid,
       metaversep0_.ou,
       metaversep0_.pwdlastset,
       metaversep0_.samaccountname,
       metaversep0_.sn,
       metaversep0_.title,
       metaversep0_.unicodepwd,
       metaversep0_.unicodepwd_hash,
       metaversep0_.useraccountcontrol,
       metaversep0_.userparameters,
       metaversep0_.userprincipalname,
       connectors1_.accountexpires,
       connectors1_.cn,
       connectors1_.department,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.employeeid,
       connectors1_.ou,
       connectors1_.pwdlastset,
       connectors1_.samaccountname,
       connectors1_.sn,
       connectors1_.syncattrs_hash,
       connectors1_.title,
       connectors1_.unicodepwd,
       connectors1_.unicodepwd_hash,
       connectors1_.useraccountcontrol,
       connectors1_.userparameters,
       connectors1_.userprincipalname
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash or metaversep0_.unicodepwd_hash is null or
       connectors1_.unicodepwd_hash is null)
  and connectors1_.placeholder = 'inner'
  and metaversep0_.pwdlastset > connectors1_.pwdlastset
order by metaversep0_.whencreated asc

-- AD 密碼需同步至 eDir

select
       connectors1_.placeholder,
       metaversep0_.accountexpires,
       metaversep0_.cn,
       metaversep0_.department,
       metaversep0_.displayname,
       metaversep0_.dn,
       metaversep0_.employeeid,
       metaversep0_.ou,
       metaversep0_.pwdlastset,
       metaversep0_.samaccountname,
       metaversep0_.sn,
       metaversep0_.title,
       metaversep0_.unicodepwd,
       metaversep0_.unicodepwd_hash,
       metaversep0_.useraccountcontrol,
       metaversep0_.userparameters,
       metaversep0_.userprincipalname,
       connectors1_.accountexpires,
       connectors1_.cn,
       connectors1_.department,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.employeeid,
       connectors1_.ou,
       connectors1_.pwdlastset,
       connectors1_.samaccountname,
       connectors1_.sn,
       connectors1_.syncattrs_hash,
       connectors1_.title,
       connectors1_.unicodepwd,
       connectors1_.unicodepwd_hash,
       connectors1_.useraccountcontrol,
       connectors1_.userparameters,
       connectors1_.userprincipalname
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash or metaversep0_.unicodepwd_hash is null or
       connectors1_.unicodepwd_hash is null)
  and connectors1_.placeholder = 'inner'
  and metaversep0_.pwdlastset < connectors1_.pwdlastset
order by metaversep0_.whencreated asc
