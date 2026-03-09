-- edir 新增人員

INSERT INTO public.metaverse_person_details (employeeid, userparameters, useraccountcontrol, sn, ou,
                                             extensionattribute1, extensionattribute2, accountexpires,
                                             userprincipalname, samaccountname, title, department, extensionattribute10,
                                             extensionattribute11, extensionattribute12, extensionattribute13,
                                             extensionattribute14, extensionattribute15, displayname, cn, unicodepwd,
                                             dn, objectguid, unicodepwd_hash, syncattrs_hash, dn_hash, whenchanged,
                                             pwdlastset, whencreated, mail, memberof)
VALUES ('18417', 'ACTIVE', 514, '邱OO', 'A030500', '1', '0', '30828-09-14 02:48:05.477000', 'NA00503@taiwanlife.cht',
        'NA00503', '測試123', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課',
        '邱OO(NA00503)', 'NA00503',
        'eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwia2lkIjoiMDEiLCJjbiI6Ik5BMDA1MDMifQ..szgP4v6O_RwoNGA-.RYQ7MOGFl9VdEkdz5LuDQvUdx0I.reEQdGydJwcQM9sDC0OjGg',
        'CN=NA00503,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht', '98975dfc2499eb4a9e67fdf321e1c5ac',
        '7a5e3f52c3320f67cceaaad4a31c2a98adf89872ca8601a891ec5dc0efc40fbc',
        '35101f5a1ee0598d73104ca98138c1212062927a35d86124faaa45c3be1c7134',
        'c0386b3e4e899a0264f9354f94e6bc8ade9a8380cb6e371389e3e85b413024c5', '2019-12-03 09:30:14.000000',
        '2019-12-03 09:30:14.000000', '2019-12-03 09:26:52.000000', null,
        'CN=A030500,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht;CN=A030500-G,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht')
on conflict (objectguid) do update set employeeid = excluded.employeeid,
                                       userparameters = excluded.userparameters,
                                       useraccountcontrol = excluded.useraccountcontrol,
                                       sn = excluded.sn,
                                       ou = excluded.ou,
                                       extensionattribute1 = excluded.extensionattribute1,
                                       extensionattribute2 = excluded.extensionattribute2,
                                       accountexpires = excluded.accountexpires,
                                       userprincipalname = excluded.userprincipalname,
                                       samaccountname = excluded.samaccountname,
                                       title              = excluded.title,
                                       department = excluded.department,
                                       extensionattribute10 = excluded.extensionattribute10,
                                       extensionattribute11 = excluded.extensionattribute11,
                                       extensionattribute12 = excluded.extensionattribute12,
                                       extensionattribute13 = excluded.extensionattribute13,
                                       extensionattribute14 = excluded.extensionattribute14,
                                       extensionattribute15 = excluded.extensionattribute15,
                                       displayname = excluded.displayname,
                                       cn = excluded.cn,
                                       unicodepwd = excluded.unicodepwd,
                                       dn = excluded.dn,
                                       unicodepwd_hash = excluded.unicodepwd_hash,
                                       syncattrs_hash = excluded.syncattrs_hash,
                                       dn_hash = excluded.dn_hash,
                                       whenchanged = excluded.whenchanged,
                                       pwdlastset = excluded.pwdlastset,
                                       whencreated = excluded.whencreated,
                                       mail = excluded.mail,
                                       memberof = excluded.memberof;
                 
-- edir 新增組織

INSERT INTO public.metaverse_organizationalunit_details (displayname, ou, dn, whenchanged, objectguid, syncattrs_hash,
                                                         dn_hash, whencreated)
VALUES ('A0306AA桃園分局綜所稅課 6666', 'A0306AA', 'OU=A0306AA,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht',
        '2019-11-08 07:08:25.000000', 'e82140dcc825884cbf2783cd89bda33b',
        '37066361207ace0585b1ad1af2ebc221ce87011a37ac4458f6f3b0bc88e36d50',
        'f83e796e1c81875fc1ba68e7bd75d96f110046f581f22ab48d23fc2ebd2d9fd9', '2019-11-08 07:08:25.000000')
on conflict (objectguid) do update set displayname    = excluded.displayname,
                                       ou             = excluded.ou,
                                       dn             = excluded.dn,
                                       whenchanged    = excluded.whenchanged,
                                       syncattrs_hash = excluded.syncattrs_hash,
                                       dn_hash        = excluded.dn_hash,
                                       whencreated    = excluded.whencreated;

-- edir 新增群組

INSERT INTO public.metaverse_group_details (dn, objectguid, cn, samaccountname, name, grouptype, memberof,
                                            extensionattribute10, extensionattribute11, extensionattribute12,
                                            extensionattribute13, extensionattribute14, extensionattribute15,
                                            mail_enabled, syncattrs_hash, dn_hash, whencreated, whenchanged)
VALUES ('CN=A0306AA,OU=A0306AA,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht', '', 'A0306AA', 'A0306AA',
        'A0306AA', -2147483640, '[CN=A030500,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht]',
        'A0306AA桃園分局綜所稅課 6666', 'A0306AA桃園分局綜所稅課 6666', 'A0306AA桃園分局綜所稅課 6666', 'A0306AA桃園分局綜所稅課 6666',
        'A0306AA桃園分局綜所稅課 6666', 'A0306AA桃園分局綜所稅課 6666', '1',
        'f8fe851f70f30b68ff4c8333d6a237663e83240623c12a5d401d26ab3d838993',
        '1bbbe21318105b93b196f35e1906ac710a8eeb139b1100e208ef7a75f3d00ed7', '2019-11-22 05:32:02.000000',
        '2019-11-22 05:32:02.000000')
on conflict (dn) do update set objectguid           = excluded.objectguid,
                               cn                   = excluded.cn,
                               samaccountname       = excluded.samaccountname,
                               name                 = excluded.name,
                               grouptype            = excluded.grouptype,
                               memberof             = excluded.memberof,
                               extensionattribute10 = excluded.extensionattribute10,
                               extensionattribute11 = excluded.extensionattribute11,
                               extensionattribute12 = excluded.extensionattribute12,
                               extensionattribute13 = excluded.extensionattribute13,
                               extensionattribute14 = excluded.extensionattribute14,
                               extensionattribute15 = excluded.extensionattribute15,
                               mail_enabled         = excluded.mail_enabled,
                               syncattrs_hash       = excluded.syncattrs_hash,
                               dn_hash              = excluded.dn_hash,
                               whencreated          = excluded.whencreated,
                               whenchanged          = excluded.whenchanged;
                 
-- ad 新增人員

INSERT INTO public.connector_space_ad_person_details (employeeid, useraccountcontrol, userparameters, sn, ou,
                                                      extensionattribute1, extensionattribute2, accountexpires,
                                                      userprincipalname, samaccountname, title, department,
                                                      extensionattribute10, extensionattribute11, extensionattribute12,
                                                      extensionattribute13, extensionattribute14, extensionattribute15,
                                                      displayname, cn, unicodepwd, dn, objectguid, unicodepwd_hash,
                                                      syncattrs_hash, dn_hash, whenchanged, pwdlastset, objectclass,
                                                      objectcategory, placeholder, whencreated, mail, memberof)
VALUES ('18417', 512, 'ACTIVE', '邱OO', 'A030500', '1', '0', '30828-09-14 02:48:05.477000', 'NA00502@taiwanlife.cht',
        'NA00502', null, '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課', '桃園分局綜所稅課',
        '邱OO(NA00502)', 'NA00502', null, 'CN=NA00502,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht',
        'c53348d47266b940b6fa4e5aa5019e77', null, 'be343c42a757ab6695f51aa2b6dccf8c73b4020448cfb594d753e8254893c144',
        '6b23800d45ca5653a7bcaeb20a5aac8ab4619256ef29f812b5dd812ea15100db', '2019-12-03 09:27:46.000000',
        '2019-12-03 09:27:46.600000', 'top;person;organizationalPerson;user',
        'CN=Person,CN=Schema,CN=Configuration,DC=taiwanlife,DC=cht', 'inner', '2019-12-03 09:27:46.000000', null,
        'CN=A030500,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht;CN=A030500-G,OU=A030500,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht')
        on conflict (objectguid, placeholder) do update set employeeid = excluded.employeeid,
                                                            useraccountcontrol = excluded.useraccountcontrol,
                                                            userparameters = excluded.userparameters,
                                                            sn = excluded.sn,
                                                            ou = excluded.ou,
                                                            userprincipalname = excluded.userprincipalname,
                                                            samaccountname = excluded.samaccountname,
                                                            title = excluded.title,
                                                            department = excluded.department,
                                                            extensionattribute10 = excluded.extensionattribute10,
                                                            extensionattribute11 = excluded.extensionattribute11,
                                                            extensionattribute12 = excluded.extensionattribute12,
                                                            extensionattribute13 = excluded.extensionattribute13,
                                                            extensionattribute14 = excluded.extensionattribute14,
                                                            extensionattribute15 = excluded.extensionattribute15,
                                                            displayname = excluded.displayname,
                                                            cn = excluded.cn,
                                                            unicodepwd = excluded.unicodepwd,
                                                            dn = excluded.dn,
                                                            unicodepwd_hash = excluded.unicodepwd_hash,
                                                            syncattrs_hash = excluded.syncattrs_hash,
                                                            dn_hash = excluded.dn_hash,
                                                            whenchanged = excluded.whenchanged,
                                                            whencreated = excluded.whencreated,
                                                            mail = excluded.mail,
                                                            memberof = excluded.memberof;
                                                            
-- ad 新增組織                                                            

INSERT INTO public.connector_space_ad_organizationalunit_details (displayname, ou, dn, objectguid, syncattrs_hash,
                                                                  dn_hash, whenchanged, objectclass, objectcategory,
                                                                  placeholder, whencreated)
VALUES (null, 'MOF', 'OU=MOF,DC=taiwanlife,DC=cht', '8b64884a44bdf74e97ee47d301a5b5e8',
        'da774d7e0bea889a65e6d5a94692c611f7572eb90901acde851ee016a993f472',
        'c07be465c043cf12ffc0fc1cf7b90e8190969d48dcdcbf0a71b705bd2ccd5f5a', '2019-10-08 02:03:29.000000',
        'top;organizationalUnit', 'CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=taiwanlife,DC=cht', 'inner',
        '2019-10-05 05:58:12.000000')
on conflict (objectguid, placeholder) do update set displayname = excluded.displayname,
                                                    ou = excluded.ou,
                                                    dn = excluded.dn,
                                                    syncattrs_hash = excluded.syncattrs_hash,
                                                    dn_hash = excluded.dn_hash,
                                                    whenchanged = excluded.whenchanged,
                                                    objectclass = excluded.objectclass,
                                                    objectcategory = excluded.objectcategory,
                                                    whencreated = excluded.whencreated;

-- ad 新增群組

INSERT INTO public.connector_space_ad_group_details (placeholder, dn, objectguid, cn, samaccountname, objectclass,
                                                     objectcategory, name, grouptype, memberof, extensionattribute10,
                                                     extensionattribute11, extensionattribute12, extensionattribute13,
                                                     extensionattribute14, extensionattribute15, whencreated,
                                                     whenchanged, mail_enabled, syncattrs_hash, dn_hash)
VALUES ('inner', 'CN=A03-G,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht', 'afeae1362cee494898ff77c7d8f3a240', 'A03-G',
        'A03-G', 'top;group', 'CN=Group,CN=Schema,CN=Configuration,DC=taiwanlife,DC=cht', 'A03-G', -2147483646,
        'CN=A03-DL,OU=A03,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht;CN=A05ROOT-G,OU=A05ROOT,OU=MOF,DC=taiwanlife,DC=cht',
        'A03', 'A03', 'A03', 'A03', 'A03', 'A03', '2019-11-27 08:38:07.000000', '2019-11-27 08:40:57.000000', '0',
        '6a32fc039f9ba8deff920b4729376f5edf4f2b53ccdd0c227cb036ef8a46dd70',
        'cb8b1d8bef6e50461f8f1ed20ccb82d4176f8aca44c2569595b063f212c642c6')
on conflict (objectguid, placeholder) do update set dn                   = excluded.dn,
                                                    cn                   = excluded.cn,
                                                    samaccountname       = excluded.samaccountname,
                                                    objectclass          = excluded.objectclass,
                                                    objectcategory       = excluded.objectcategory,
                                                    name                 = excluded.name,
                                                    grouptype            = excluded.grouptype,
                                                    memberof             = excluded.memberof,
                                                    extensionattribute10 = excluded.extensionattribute10,
                                                    extensionattribute11 = excluded.extensionattribute11,
                                                    extensionattribute12 = excluded.extensionattribute12,
                                                    extensionattribute13 = excluded.extensionattribute13,
                                                    extensionattribute14 = excluded.extensionattribute14,
                                                    extensionattribute15 = excluded.extensionattribute15,
                                                    whencreated          = excluded.whencreated,
                                                    whenchanged          = excluded.whenchanged,
                                                    mail_enabled         = excluded.mail_enabled,
                                                    syncattrs_hash       = excluded.syncattrs_hash,
                                                    dn_hash              = excluded.dn_hash;



                                      
