update connector_space_ad_person_details
set unicodepwd      = metaverse_person_details.unicodepwd,
    unicodepwd_hash = metaverse_person_details.unicodepwd_hash
from metaverse_person_details
where connector_space_ad_person_details.dn = metaverse_person_details.dn 
and metaverse_person_details.placeholder = 'inner'
and connector_space_ad_person_details.placeholder = 'inner'
and connector_space_ad_person_details.unicodepwd is null

update connector_space_ad_person_details
set unicodepwd      = metaverse_person_details.unicodepwd,
    unicodepwd_hash = metaverse_person_details.unicodepwd_hash
from metaverse_person_details
where connector_space_ad_person_details.dn = metaverse_person_details.dn 
and metaverse_person_details.placeholder = 'external'
and connector_space_ad_person_details.placeholder = 'external'
and connector_space_ad_person_details.unicodepwd is null