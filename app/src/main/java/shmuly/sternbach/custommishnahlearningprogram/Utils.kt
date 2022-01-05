package shmuly.sternbach.custommishnahlearningprogram

import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit

object Utils {
    fun getOverviewString(units: List<ProgramUnit>): String {
        if(units.size == 1) return units.first().material
        if(units.size == 2) return units.first().material + " - " + units[1].material
        val stringBuilder = StringBuilder()
        var counter = 0
        var firstUnitInGroup = units[counter++]
        //1,1,1,1,2,2,2,2,3,3,3,4,5,6
        var previousUnit = firstUnitInGroup
        var thisUnit = units[counter]
        while(counter < units.size) {
            val sameGroupAsPrevious = previousUnit.group == thisUnit.group
            val lastElementInSet = counter == units.size - 1
            if(sameGroupAsPrevious && !lastElementInSet) {
                previousUnit = thisUnit
                thisUnit = units[++counter]
            }
            else {
                if(!sameGroupAsPrevious && lastElementInSet) {
                    if(firstUnitInGroup.material == previousUnit.material/*the group which came before this was 1 long*/)
                    {
                        stringBuilder.appendLine(firstUnitInGroup.material)
                    } else stringBuilder.appendLine(
                        "${firstUnitInGroup.material} - ${
                            if(sameGroupAsPrevious /*last one in list*/) thisUnit.material else previousUnit.material
                        }"
                    ) //not sure why I have to do this, but it makes the tests pass
                    stringBuilder.append(thisUnit.material)
                    break
                }
                if(firstUnitInGroup.material == previousUnit.material || firstUnitInGroup.material == thisUnit.material/*not sure this can ever be true if the first is false, but just for good measure*/) /*only 1 unit long/1 unit to learn/review*/
                {
                    stringBuilder.append(firstUnitInGroup.material)
                }
                else
                    stringBuilder.append(
                    "${firstUnitInGroup.material} - ${
                        if(sameGroupAsPrevious /*last one in list*/) thisUnit.material else previousUnit.material
                    }"
                )
                if(!lastElementInSet) stringBuilder.append('\n')
                else break
                firstUnitInGroup = thisUnit
                previousUnit = firstUnitInGroup
            }
        }
        return stringBuilder.toString()
    }
}