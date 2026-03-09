-- 人員新增

select metaversep0_.objectguid           ,
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
from metaverse_person_details metaversep0_
where not (exists(select (connectors1_.objectguid, connectors1_.placeholder)
                  from connector_space_ad_person_details connectors1_
                  where metaversep0_.dn = connectors1_.dn
                    and connectors1_.placeholder='inner'))
order by metaversep0_.whencreated asc

-- 人員組織異動

select metaversep0_.objectguid,
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
       connectors1_.whencreated
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.cn = connectors1_.cn
  and metaversep0_.dn_hash <> connectors1_.dn_hash
  and connectors1_.placeholder = 'inner'
order by metaversep0_.whencreated asc

-- 人員屬性異動

select metaversep0_.objectguid,
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
       connectors1_.whencreated
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and metaversep0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaversep0_.whencreated asc

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
           when metaversep0_.cn = connectors1_.cn then 'true'
           else 'false' end as cn,
       case
           when metaversep0_.department = connectors1_.department then 'true'
           else 'false' end as department,
       case
           when metaversep0_.displayname = connectors1_.displayname then 'true'
           else 'false' end as displayname,
       case
           when metaversep0_.dn = connectors1_.dn then 'true'
           else 'false' end as dn,
       case
           when metaversep0_.employeeid = connectors1_.employeeid then 'true'
           else 'false' end as employeeid,
       case
           when metaversep0_.extensionattribute1 = connectors1_.extensionattribute1 then 'true'
           else 'false' end as extensionattribute1,
       case
           when metaversep0_.extensionattribute2 = connectors1_.extensionattribute2 then 'true'
           else 'false' end as extensionattribute2,
       case
           when metaversep0_.extensionattribute10 = connectors1_.extensionattribute10 then 'true'
           else 'false' end as extensionattribute10,
       case
           when metaversep0_.extensionattribute11 = connectors1_.extensionattribute11 then 'true'
           else 'false' end as extensionattribute11,
       case
           when metaversep0_.extensionattribute12 = connectors1_.extensionattribute12 then 'true'
           else 'false' end as extensionattribute12,
       case
           when metaversep0_.extensionattribute13 = connectors1_.extensionattribute13 then 'true'
           else 'false' end as extensionattribute13,
       case
           when metaversep0_.extensionattribute14 = connectors1_.extensionattribute14 then 'true'
           else 'false' end as extensionattribute14,
       case
           when metaversep0_.extensionattribute15 = connectors1_.extensionattribute15 then 'true'
           else 'false' end as extensionattribute15,
       case
           when metaversep0_.mail = connectors1_.mail then 'true'
           else 'false' end as mail,
       case
           when metaversep0_.memberof = connectors1_.memberof then 'true'
           else 'false' end as memberof,
       case
           when metaversep0_.ou = connectors1_.ou then 'true'
           else 'false' end as ou,
       case
           when metaversep0_.pwdlastset = connectors1_.pwdlastset then 'true'
           else 'false' end as pwdlastset,
       case
           when metaversep0_.samaccountname = connectors1_.samaccountname then 'true'
           else 'false' end as samaccountname,
       case
           when metaversep0_.sn = connectors1_.sn then 'true'
           else 'false' end as sn,
       case
           when metaversep0_.syncattrs_hash = connectors1_.syncattrs_hash then 'true'
           else 'false' end as syncattrs_hash,
       case
           when metaversep0_.title = connectors1_.title then 'true'
           else 'false' end as title,
       case
           when metaversep0_.unicodepwd = connectors1_.unicodepwd then 'true'
           else 'false' end as unicodepwd,
       case
           when metaversep0_.useraccountcontrol = connectors1_.useraccountcontrol then 'true'
           else 'false' end as useraccountcontrol,
       case
           when metaversep0_.userparameters = connectors1_.userparameters then 'true'
           else 'false' end as userparameters,
       case
           when metaversep0_.userprincipalname = connectors1_.userprincipalname then 'true'
           else 'false' end as userprincipalname
from metaverse_person_details metaversep0_
         cross join connector_space_ad_person_details connectors1_
where metaversep0_.dn = connectors1_.dn
  and metaversep0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaversep0_.whencreated asc

-- 查詢 AD 幽靈帳號 (啟用狀態 <> 514)

select connectors0_.objectguid,
       connectors0_.placeholder,
       connectors0_.accountexpires,
       connectors0_.cn,
       connectors0_.department,
       connectors0_.displayname,
       connectors0_.dn,
       connectors0_.dn_hash,
       connectors0_.employeeid,
       connectors0_.extensionattribute1,
       connectors0_.extensionattribute10,
       connectors0_.extensionattribute11,
       connectors0_.extensionattribute12,
       connectors0_.extensionattribute13,
       connectors0_.extensionattribute14,
       connectors0_.extensionattribute15,
       connectors0_.extensionattribute2,
       connectors0_.mail,
       connectors0_.memberof,
       connectors0_.objectcategory,
       connectors0_.objectclass,
       connectors0_.ou,
       connectors0_.pwdlastset,
       connectors0_.samaccountname,
       connectors0_.sn,
       connectors0_.syncattrs_hash,
       connectors0_.title,
       connectors0_.unicodepwd,
       connectors0_.unicodepwd_hash,
       connectors0_.useraccountcontrol,
       connectors0_.userparameters,
       connectors0_.userprincipalname,
       connectors0_.whenchanged,
       connectors0_.whencreated
from connector_space_ad_person_details connectors0_
where not (exists(select metaversep1_.objectguid
                  from metaverse_person_details metaversep1_
                  where connectors0_.cn = metaversep1_.cn))
  and (connectors0_.dn like '%DC=fia,DC=gov,DC=tw')
  and connectors0_.useraccountcontrol <> 514
  and connectors0_.placeholder = 'inner'
order by connectors0_.whencreated asc
