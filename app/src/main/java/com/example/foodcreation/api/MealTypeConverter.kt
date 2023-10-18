package com.example.foodcreation.api

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attributes: Any?): String{ // it will be used by the database, when it wants to insert in the table

        if(attributes == null){ return "" }

        else { return attributes as String}
    }

    @TypeConverter
    fun fromStringToAny(attributes: String?): Any{ // it will use the database, when it wants to retrive the data from the table.

        if(attributes == null){ return "" }

        else { return attributes  }
    }


}