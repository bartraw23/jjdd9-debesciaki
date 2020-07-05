<div class="container">
    <#include "sneakPeak.ftlh">
    <div class="jumbotron ">
        <h3 class="display-4 text-center">Lista wydarzeń</h3>
        <#include "searchInput.ftlh"><br>
        <table id="dtBasicExample" class="table table-striped table-bordered table-hover table-sm bg-white"  cellspacing="0">
            <thead>
            <tr class="bg-dark text-white">
                <th class="th-sm text-center" width="40%">Nazwa</th>
                <th class="th-sm text-center" width="25%">Organizator</th>
                <th class="th-sm text-center" width="15%">Data rozpoczęcia</th>
                <th class="th-sm text-center" width="15%">Data zakończenia</th>
                <#if role == "USER" || role == "ADMIN" || role == "SUPER_ADMIN"><th class="th-sm text-center" width="5%"></th></#if>
            </tr>
            </thead>
            <tbody>
            <#list events as event>
                <tr>
                    <td class="text-primary font-weight-bold"><a href="/show-one-event?event=${event.id}">${event.name}</a></td>
                    <td class="text-center">${event.organizerName}</td>
                    <td class="text-center">${event.startDateAll}</td>
                    <td class="text-center">${event.endDateAll}</td>
                    <#if role == "USER" || role == "ADMIN" || role == "SUPER_ADMIN"><td class="text-center"><nobr><#include "buttons/favouriteButton.ftlh"><#if role == "ADMIN" || role == "SUPER_ADMIN"><#include "buttons/editButton.ftlh">&nbsp;<#include "buttons/deleteButton.ftlh"></#if></nobr></td></#if>
                </tr>
            </#list>
            </tbody>
        </table>
        <#if numberOfEvents gt 20>
        <#include "navigationUnderTable.ftlh">
        </#if>
    </div>
</div>