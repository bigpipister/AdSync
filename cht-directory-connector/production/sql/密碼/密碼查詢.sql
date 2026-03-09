-- 內:密碼查詢

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from
	metaverse_person_details metaversep0_
cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
	and metaversep0_.pwdlastset <> connectors1_.pwdlastset
	and connectors1_.placeholder = 'inner'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc

-- 內:YBD密碼更新至AD密碼

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from
	metaverse_person_details metaversep0_
cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
	and metaversep0_.pwdlastset > connectors1_.pwdlastset
	and connectors1_.placeholder = 'inner'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc

-- 內:AD密碼更新至YBD密碼

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from
	metaverse_person_details metaversep0_
cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
	and metaversep0_.pwdlastset < connectors1_.pwdlastset
	and connectors1_.placeholder = 'inner'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc

-- 外:密碼查詢 (TAX)

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from metaverse_person_details_external metaversep0_
         cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
  	and metaversep0_.placeholder = 'external'
	and connectors1_.placeholder = 'external'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc

-- 外:YBD密碼更新AD密碼(TAX)

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from metaverse_person_details_external metaversep0_
         cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
    and metaversep0_.pwdlastset > connectors1_.pwdlastset
  	and metaversep0_.placeholder = 'external'
	and connectors1_.placeholder = 'external'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc
    

-- 外:AD密碼更新YBD密碼(TAX)

select
	metaversep0_.cn ,
	metaversep0_.dn ,
	metaversep0_.unicodepwd,
	metaversep0_.pwdlastset,
    extract(epoch from metaversep0_.pwdlastset),
	connectors1_.cn ,
	connectors1_.dn ,
	connectors1_.unicodepwd,
	connectors1_.pwdlastset,
    extract(epoch from connectors1_.pwdlastset)
from metaverse_person_details_external metaversep0_
         cross join connector_space_ad_person_details connectors1_
where
	metaversep0_.dn = connectors1_.dn
	and (metaversep0_.unicodepwd_hash <> connectors1_.unicodepwd_hash
	or metaversep0_.unicodepwd_hash is null
	or connectors1_.unicodepwd_hash is null)
    and metaversep0_.pwdlastset > connectors1_.pwdlastset
  	and metaversep0_.placeholder = 'external'
	and connectors1_.placeholder = 'external'
    and (lower(metaversep0_.dn) like lower(('%OU=MOF,DC=fia,DC=gov,DC=tw%')))
order by
	metaversep0_.whencreated asc    