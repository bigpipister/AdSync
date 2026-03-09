# Import AD Objects to new environment
Import-Module ActiveDirectory
# First, import the CSV
$ADObjects = Import-Csv -Path "C:\Users\Administrator\Documents\ADUsers_Export.csv"

# Loop through each object and create in new AD
foreach ($Object in $ADObjects) {
    try {
        # Create new AD object based on objectClass
        switch ($Object.objectClass) {
            "user" {
                New-ADUser -Name $Object.Name `
                           -Department $Object.Department `
                           -DisplayName $Object.DisplayName `
                           -EmployeeID $Object.EmployeeID `
                           -Title $Object.Title `
                           -Surname $Object.sn `
                           -UserPrincipalName $Object.UserPrincipalName `
                           -Path $Object.DistinguishedName.Substring($Object.DistinguishedName.IndexOf(',')+1) `
                           -AccountPassword (ConvertTo-SecureString -AsPlainText "!QAZxsw2#EDC" -Force) `
                           -Enabled $true `
                           -OtherAttributes @{
                               #mail = $(if ($null -eq $Object.mail) {'-'} else {$Object.mail})
                               pager = $Object.pager
                               extensionAttribute1 = $(if ($null -eq $Object.extensionAttribute1) {'-'} else {$Object.extensionAttribute1})
                               extensionAttribute2 = $(if ($null -eq $Object.extensionAttribute2) {'-'} else {$Object.extensionAttribute2})
                               extensionAttribute10 = $(if ($null -eq $Object.extensionAttribute10) {'-'} else {$Object.extensionAttribute10})
                               extensionAttribute11 = $(if ($null -eq $Object.extensionAttribute11) {'-'} else {$Object.extensionAttribute11})
                               extensionAttribute12 = $(if ($null -eq $Object.extensionAttribute12) {'-'} else {$Object.extensionAttribute12})
                               extensionAttribute13 = $(if ($null -eq $Object.extensionAttribute13) {'-'} else {$Object.extensionAttribute13})
                               extensionAttribute14 = $(if ($null -eq $Object.extensionAttribute14) {'-'} else {$Object.extensionAttribute14})
                               extensionAttribute15 = $(if ($null -eq $Object.extensionAttribute14) {'-'} else {$Object.extensionAttribute15})
                           }
                
                # 如果要加入群組，建議使用 Add-ADGroupMember 指令
                $groups = $Object.GroupNames -split " "
                foreach ($group in $groups) {
                    Add-ADGroupMember -Identity $group -Members $Object.SamAccountName
                }
                
            }
            "group" {
                New-ADGroup -Name $Object.Name `
                           -DisplayName $Object.DisplayName `
                           -GroupScope $Object.GroupScope `
                           -Path $Object.DistinguishedName.Substring($Object.DistinguishedName.IndexOf(',')+1) `
                           -OtherAttributes @{
                               # when group is disabled, mail will be empty and then add fail
                               #mail = $(if ($null -eq $Object.mail) {'-'} else {$Object.mail})
                               extensionAttribute10 = $(if ($null -eq $Object.extensionAttribute10) {'-'} else {$Object.extensionAttribute10})
                               extensionAttribute11 = $(if ($null -eq $Object.extensionAttribute11) {'-'} else {$Object.extensionAttribute11})
                               extensionAttribute12 = $(if ($null -eq $Object.extensionAttribute12) {'-'} else {$Object.extensionAttribute12})
                               extensionAttribute13 = $(if ($null -eq $Object.extensionAttribute13) {'-'} else {$Object.extensionAttribute13})
                               extensionAttribute14 = $(if ($null -eq $Object.extensionAttribute14) {'-'} else {$Object.extensionAttribute14})
                               extensionAttribute15 = $(if ($null -eq $Object.extensionAttribute14) {'-'} else {$Object.extensionAttribute15})
                           }

                # 如果要加入群組，建議使用 Add-ADGroupMember 指令
                $groups = $Object.GroupNames -split " "
                foreach ($group in $groups) {
                    Add-ADGroupMember -Identity $group -Members $Object.Name
                }
            }
            "organizationalUnit" {
                New-ADOrganizationalUnit -Name $Object.Name `
                                        -DisplayName $Object.DisplayName `
                                        -Path $Object.DistinguishedName.Substring($Object.DistinguishedName.IndexOf(',')+1)
            }
        }
    }
    catch {
        Write-Warning "Failed to create object: $($Object.Name). Error: $_"
    }
}