-- 群組新增

select metaverseg0_.dn,
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
from metaverse_group_details metaverseg0_
where not (exists(select (connectors1_.dn, connectors1_.placeholder)
                  from connector_space_ad_group_details connectors1_
                  where metaverseg0_.dn = connectors1_.dn
                    and connectors1_.placeholder = 'inner'))
order by metaverseg0_.whencreated asc, length(metaverseg0_.dn) asc

-- 群組異動

select metaverseg0_.dn,
       connectors1_.dn,
       connectors1_.placeholder,
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
       metaverseg0_.whencreated,
       connectors1_.cn,
       connectors1_.dn_hash,
       connectors1_.extensionattribute10,
       connectors1_.extensionattribute11,
       connectors1_.extensionattribute12,
       connectors1_.extensionattribute13,
       connectors1_.extensionattribute14,
       connectors1_.extensionattribute15,
       connectors1_.grouptype,
       connectors1_.mail_enabled,
       connectors1_.memberof,
       connectors1_.name,
       connectors1_.objectcategory,
       connectors1_.objectclass,
       connectors1_.objectguid,
       connectors1_.samaccountname,
       connectors1_.syncattrs_hash,
       connectors1_.whenchanged,
       connectors1_.whencreated
from metaverse_group_details metaverseg0_
         cross join connector_space_ad_group_details connectors1_
where metaverseg0_.dn = connectors1_.dn
  and metaverseg0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaverseg0_.whencreated asc, length(metaverseg0_.dn) asc

select metaverseg0_.dn,
       connectors1_.dn,
       connectors1_.placeholder,
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
       metaverseg0_.whencreated,
       connectors1_.cn,
       connectors1_.dn_hash,
       connectors1_.extensionattribute10,
       connectors1_.extensionattribute11,
       connectors1_.extensionattribute12,
       connectors1_.extensionattribute13,
       connectors1_.extensionattribute14,
       connectors1_.extensionattribute15,
       connectors1_.grouptype,
       connectors1_.mail_enabled,
       connectors1_.memberof,
       connectors1_.name,
       connectors1_.objectcategory,
       connectors1_.objectclass,
       connectors1_.objectguid,
       connectors1_.samaccountname,
       connectors1_.syncattrs_hash,
       connectors1_.whenchanged,
       connectors1_.whencreated,
       case
           when metaverseg0_.extensionattribute10 = connectors1_.extensionattribute10 then 'true'
           else 'false' end as extensionattribute10,
       case
           when metaverseg0_.extensionattribute11 = connectors1_.extensionattribute11 then 'true'
           else 'false' end as extensionattribute11,
       case
           when metaverseg0_.extensionattribute12 = connectors1_.extensionattribute12 then 'true'
           else 'false' end as extensionattribute12,
       case
           when metaverseg0_.extensionattribute13 = connectors1_.extensionattribute13 then 'true'
           else 'false' end as extensionattribute13,
       case
           when metaverseg0_.extensionattribute14 = connectors1_.extensionattribute14 then 'true'
           else 'false' end as extensionattribute14,
       case
           when metaverseg0_.extensionattribute15 = connectors1_.extensionattribute15 then 'true'
           else 'false' end as extensionattribute15,
       case
           when metaverseg0_.grouptype = connectors1_.grouptype then 'true'
           else 'false' end as grouptype,
       case
           when metaverseg0_.mail_enabled = connectors1_.mail_enabled then 'true'
           else 'false' end as mail_enabled,
       case
           when metaverseg0_.memberof = connectors1_.memberof then 'true'
           else 'false' end as memberof,
       case
           when metaverseg0_.name = connectors1_.name then 'true'
           else 'false' end as name,
       case
           when metaverseg0_.samaccountname = connectors1_.samaccountname then 'true'
           else 'false' end as samaccountname
from metaverse_group_details metaverseg0_
         cross join connector_space_ad_group_details connectors1_
where metaverseg0_.dn = connectors1_.dn
  and metaverseg0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaverseg0_.whencreated asc, length(metaverseg0_.dn) asc
