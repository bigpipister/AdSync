-- 查詢是否有存在多個相同 cn 的使用者

SELECT cn, count(*)
FROM inidm.metaverse_person_details group by cn having count(*) > 1;
