-- 組織新增

select metaverseo0_.objectguid,     
       metaverseo0_.displayname,    
       metaverseo0_.dn,             
       metaverseo0_.dn_hash,        
       metaverseo0_.ou,             
       metaverseo0_.syncattrs_hash, 
       metaverseo0_.whenchanged,    
       metaverseo0_.whencreated  
from metaverse_organizationalunit_details metaverseo0_
where not (exists(select (connectors1_.objectguid, connectors1_.placeholder)
                  from connector_space_ad_organizationalunit_details connectors1_
                  where metaverseo0_.dn = connectors1_.dn
                    and connectors1_.placeholder = 'inner'))
order by metaverseo0_.whencreated asc, length(metaverseo0_.dn) asc

-- 組織異動

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
       connectors1_.whencreated
from metaverse_organizationalunit_details metaverseo0_
         cross join connector_space_ad_organizationalunit_details connectors1_
where metaverseo0_.dn = connectors1_.dn
  and metaverseo0_.syncattrs_hash <> connectors1_.syncattrs_hash
  and connectors1_.placeholder = 'inner'
order by metaverseo0_.whencreated asc, length(metaverseo0_.dn) asc

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
order by metaverseo0_.whencreated asc, length(metaverseo0_.dn) asc
