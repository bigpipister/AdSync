-- 內:人員組織異動
select metaversep0_.objectguid,
       metaversep0_.placeholder,
       connectors1_.objectguid,
       connectors1_.placeholder,
       metaversep0_.accountexpires,
       metaversep0_.cn,
       metaversep0_.department,
       metaversep0_.displayname,
       metaversep0_.dn,
       metaversep0_.dn_hash,
       metaversep0_.employeeid,
       metaversep0_.extensionattribute1,
       metaversep0_.extensionattribute10,
       metaversep0_.extensionattribute11,
       metaversep0_.extensionattribute12,
       metaversep0_.extensionattribute13,
       metaversep0_.extensionattribute14,
       metaversep0_.extensionattribute15,
       metaversep0_.extensionattribute2,
       metaversep0_.mail,
       metaversep0_.memberof,
       metaversep0_.ou,
       metaversep0_.pwdlastset,
       metaversep0_.samaccountname,
       metaversep0_.sn,
       metaversep0_.syncattrs_hash,
       metaversep0_.title,
       metaversep0_.unicodepwd,
       metaversep0_.unicodepwd_hash,
       metaversep0_.useraccountcontrol,
       metaversep0_.userparameters,
       metaversep0_.userprincipalname,
       metaversep0_.whenchanged,
       metaversep0_.whencreated,
       connectors1_.accountexpires,
       connectors1_.cn,
       connectors1_.department,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.dn_hash connectors1_.employeeid,
       connectors1_.extensionattribute1,
       connectors1_.extensionattribute10,
       connectors1_.extensionattribute11,
       connectors1_.extensionattribute12,
       connectors1_.extensionattribute13,
       connectors1_.extensionattribute14,
       connectors1_.extensionattribute15,
       connectors1_.extensionattribute2,
       connectors1_.mail,
       connectors1_.memberof,
       connectors1_.objectcategory,
       connectors1_.objectclass,
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
       connectors1_.userprincipalname,
       connectors1_.whenchanged,
       connectors1_.whencreated
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.cn = connectors1_.cn
  and metaversep0_.dn_hash <> connectors1_.dn_hash
  and connectors1_.placeholder = 'inner'
  and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by metaversep0_.whencreated asc

-- 內:人員異動
select metaversep0_.accountexpires,
       metaversep0_.cn,
       metaversep0_.department,
       metaversep0_.displayname,
       metaversep0_.dn,
       metaversep0_.dn_hash,
       metaversep0_.employeeid,
       metaversep0_.extensionattribute1,
       metaversep0_.extensionattribute10,
       metaversep0_.extensionattribute11,
       metaversep0_.extensionattribute12,
       metaversep0_.extensionattribute13,
       metaversep0_.extensionattribute14,
       metaversep0_.extensionattribute15,
       metaversep0_.extensionattribute2,
       metaversep0_.mail,
       metaversep0_.memberof,
       metaversep0_.ou,
       metaversep0_.pwdlastset,
       metaversep0_.samaccountname,
       metaversep0_.sn,
       metaversep0_.syncattrs_hash,
       metaversep0_.title,
       metaversep0_.unicodepwd,
       metaversep0_.unicodepwd_hash,
       metaversep0_.useraccountcontrol,
       metaversep0_.userparameters,
       metaversep0_.userprincipalname,
       metaversep0_.whenchanged,
       metaversep0_.whencreated,
       connectors1_.accountexpires,
       connectors1_.cn,
       connectors1_.department,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.dn_hash,
       connectors1_.employeeid,
       connectors1_.extensionattribute1,
       connectors1_.extensionattribute10,
       connectors1_.extensionattribute11,
       connectors1_.extensionattribute12,
       connectors1_.extensionattribute13,
       connectors1_.extensionattribute14,
       connectors1_.extensionattribute15,
       connectors1_.extensionattribute2,
       connectors1_.mail,
       connectors1_.memberof,
       connectors1_.objectcategory,
       connectors1_.objectclass,
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
       connectors1_.userprincipalname,
       connectors1_.whenchanged,
       connectors1_.whencreated,
       case
           when metaversep0_.accountexpires = connectors1_.accountexpires then 'true'
           else 'false' end as accountexpires,
       case
           when coalesce(metaversep0_.cn, '') = coalesce(connectors1_.cn, '') then 'true'
           else 'false' end as cn,
       case
           when coalesce(metaversep0_.department, '') = coalesce(connectors1_.department, '') then 'true'
           else 'false' end as department,
       case
           when coalesce(metaversep0_.displayname, '') = coalesce(connectors1_.displayname, '') then 'true'
           else 'false' end as displayname,
       case
           when coalesce(metaversep0_.dn, '') = coalesce(connectors1_.dn, '') then 'true'
           else 'false' end as dn,
       case
           when coalesce(metaversep0_.employeeid, '') = coalesce(connectors1_.employeeid, '') then 'true'
           else 'false' end as employeeid,
       case
           when coalesce(metaversep0_.extensionattribute1, '') = coalesce(connectors1_.extensionattribute1, '')
               then 'true'
           else 'false' end as extensionattribute1,
       case
           when coalesce(metaversep0_.extensionattribute2, '') = coalesce(connectors1_.extensionattribute2, '')
               then 'true'
           else 'false' end as extensionattribute2,
       case
           when coalesce(metaversep0_.extensionattribute10, '') = coalesce(connectors1_.extensionattribute10, '')
               then 'true'
           else 'false' end as extensionattribute10,
       case
           when coalesce(metaversep0_.extensionattribute11, '') = coalesce(connectors1_.extensionattribute11, '')
               then 'true'
           else 'false' end as extensionattribute11,
       case
           when coalesce(metaversep0_.extensionattribute12, '') = coalesce(connectors1_.extensionattribute12, '')
               then 'true'
           else 'false' end as extensionattribute12,
       case
           when coalesce(metaversep0_.extensionattribute13, '') = coalesce(connectors1_.extensionattribute13, '')
               then 'true'
           else 'false' end as extensionattribute13,
       case
           when coalesce(metaversep0_.extensionattribute14, '') = coalesce(connectors1_.extensionattribute14, '')
               then 'true'
           else 'false' end as extensionattribute14,
       case
           when coalesce(metaversep0_.extensionattribute15, '') = coalesce(connectors1_.extensionattribute15, '')
               then 'true'
           else 'false' end as extensionattribute15,
       case
           when coalesce(metaversep0_.mail, '') = coalesce(connectors1_.mail, '') then 'true'
           else 'false' end as mail,
       case
           when coalesce(metaversep0_.memberof, '') = coalesce(connectors1_.memberof, '') then 'true'
           else 'false' end as memberof,
       case
           when coalesce(metaversep0_.ou, '') = coalesce(connectors1_.ou, '') then 'true'
           else 'false' end as ou,
       case
           when metaversep0_.pwdlastset = connectors1_.pwdlastset then 'true'
           else 'false' end as pwdlastset,
       case
           when coalesce(metaversep0_.samaccountname, '') = coalesce(connectors1_.samaccountname, '') then 'true'
           else 'false' end as samaccountname,
       case
           when coalesce(metaversep0_.sn, '') = coalesce(connectors1_.sn, '') then 'true'
           else 'false' end as sn,
       case
           when coalesce(metaversep0_.syncattrs_hash, '') = coalesce(connectors1_.syncattrs_hash, '') then 'true'
           else 'false' end as syncattrs_hash,
       case
           when coalesce(metaversep0_.title, '') = coalesce(connectors1_.title, '') then 'true'
           else 'false' end as title,
       case
           when coalesce(metaversep0_.unicodepwd, '') = coalesce(connectors1_.unicodepwd, '') then 'true'
           else 'false' end as unicodepwd,
       case
           when metaversep0_.useraccountcontrol = connectors1_.useraccountcontrol then 'true'
           else 'false' end as useraccountcontrol,
       case
           when coalesce(metaversep0_.userparameters, '') = coalesce(connectors1_.userparameters, '') then 'true'
           else 'false' end as userparameters,
       case
           when coalesce(metaversep0_.userprincipalname, '') = coalesce(connectors1_.userprincipalname, '') then 'true'
           else 'false' end as userprincipalname
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and metaversep0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
  and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=taiwanlife,DC=cht%')))
order by metaversep0_.whencreated asc;

-- 內:組織dn異動
select metaverseo0_.ou,
       metaverseo0_.placeholder,
       connectors1_.ou,
       connectors1_.placeholder,
       metaverseo0_.displayname,
       metaverseo0_.dn,
       metaverseo0_.dn_hash,
       metaverseo0_.objectguid,
       metaverseo0_.syncattrs_hash,
       metaverseo0_.whenchanged,
       metaverseo0_.whencreated,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.dn_hash,
       connectors1_.objectcategory,
       connectors1_.objectclass,
       connectors1_.objectguid,
       connectors1_.syncattrs_hash,
       connectors1_.whenchanged,
       connectors1_.whencreated
from metaverse_organizationalunit_details metaverseo0_
         cross join
     connector_space_ad_organizationalunit_details connectors1_
where metaverseo0_.ou = connectors1_.ou
  and metaverseo0_.dn_hash <> connectors1_.dn_hash
  and connectors1_.placeholder = 'inner'
  and (
    lower(metaverseo0_.dn) like lower(('%' ||?|| '%'))
    )
order by metaverseo0_.whencreated asc


-- 內:組織屬性異動
select metaverseo0_.objectguid,
       connectors1_.objectguid,
       connectors1_.placeholder,
       metaverseo0_.displayname,
       metaverseo0_.dn,
       metaverseo0_.dn_hash,
       metaverseo0_.ou,
       metaverseo0_.syncattrs_hash,
       metaverseo0_.whenchanged,
       metaverseo0_.whencreated,
       connectors1_.displayname,
       connectors1_.dn,
       connectors1_.dn_hash,
       connectors1_.objectcategory,
       connectors1_.objectclass,
       connectors1_.ou,
       connectors1_.syncattrs_hash,
       connectors1_.whenchanged,
       connectors1_.whencreated,
       case
           when metaverseo0_.displayname = connectors1_.displayname then 'true'
           else 'false' end as displayname,
       case
           when metaverseo0_.dn = connectors1_.dn then 'true'
           else 'false' end as dn,
       case
           when metaverseo0_.ou = connectors1_.ou then 'true'
           else 'false' end as ou,
       case
           when metaverseo0_.syncattrs_hash = connectors1_.syncattrs_hash then 'true'
           else 'false' end as syncattrs_hash
from metaverse_organizationalunit_details metaverseo0_
         cross join connector_space_ad_organizationalunit_details connectors1_
where metaverseo0_.dn = connectors1_.dn
  and metaverseo0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaverseo0_.whencreated asc, length(metaverseo0_.dn) asc;

-- 內:群組異動
select metaverseg0_.dn                   ,
       metaverseg0_.placeholder          ,
       connectors1_.cn                   ,
       connectors1_.placeholder          ,
       metaverseg0_.cn                   ,
       metaverseg0_.dn_hash              ,
       metaverseg0_.extensionattribute10 ,
       metaverseg0_.extensionattribute11 ,
       metaverseg0_.extensionattribute12 ,
       metaverseg0_.extensionattribute13 ,
       metaverseg0_.extensionattribute14 ,
       metaverseg0_.extensionattribute15 ,
       metaverseg0_.grouptype            ,
       metaverseg0_.mail_enabled         ,
       metaverseg0_.memberof             ,
       metaverseg0_.name                 ,
       metaverseg0_.objectguid           ,
       metaverseg0_.samaccountname       ,
       metaverseg0_.syncattrs_hash       ,
       metaverseg0_.whenchanged          ,
       metaverseg0_.whencreated          ,
       connectors1_.dn                   ,
       connectors1_.dn_hash              ,
       connectors1_.extensionattribute10 ,
       connectors1_.extensionattribute11 ,
       connectors1_.extensionattribute12 ,
       connectors1_.extensionattribute13 ,
       connectors1_.extensionattribute14 ,
       connectors1_.extensionattribute15 ,
       connectors1_.grouptype            ,
       connectors1_.mail_enabled         ,
       connectors1_.memberof             ,
       connectors1_.name                 ,
       connectors1_.objectcategory       ,
       connectors1_.objectclass          ,
       connectors1_.objectguid           ,
       connectors1_.samaccountname       ,
       connectors1_.syncattrs_hash       ,
       connectors1_.whenchanged          ,
       connectors1_.whencreated          ,
       case
           when metaverseg0_.extensionattribute10 = connectors1_.extensionattribute10 then 'true'
           else 'false' end              as extensionattribute10,
       case
           when metaverseg0_.extensionattribute11 = connectors1_.extensionattribute11 then 'true'
           else 'false' end              as extensionattribute11,
       case
           when metaverseg0_.extensionattribute12 = connectors1_.extensionattribute12 then 'true'
           else 'false' end              as extensionattribute12,
       case
           when metaverseg0_.extensionattribute13 = connectors1_.extensionattribute13 then 'true'
           else 'false' end              as extensionattribute13,
       case
           when metaverseg0_.extensionattribute14 = connectors1_.extensionattribute14 then 'true'
           else 'false' end              as extensionattribute14,
       case
           when metaverseg0_.extensionattribute15 = connectors1_.extensionattribute15 then 'true'
           else 'false' end              as extensionattribute15,
       case
           when metaverseg0_.grouptype = connectors1_.grouptype then 'true'
           else 'false' end              as grouptype,
       case
           when metaverseg0_.mail_enabled = connectors1_.mail_enabled then 'true'
           else 'false' end              as mail_enabled,
       case
           when metaverseg0_.memberof = connectors1_.memberof then 'true'
           else 'false' end              as memberof,
       case
           when metaverseg0_.name = connectors1_.name then 'true'
           else 'false' end              as name,
       case
           when metaverseg0_.samaccountname = connectors1_.samaccountname then 'true'
           else 'false' end              as samaccountname
from metaverse_group_details metaverseg0_
         cross join connector_space_ad_group_details connectors1_
where metaverseg0_.dn = connectors1_.dn
  and metaverseg0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
  and (lower(metaverseg0_.dn) like lower(('%OU=MOF,DC=taiwanlife,DC=cht%')))
order by metaverseg0_.whencreated asc, length(metaverseg0_.dn) asc