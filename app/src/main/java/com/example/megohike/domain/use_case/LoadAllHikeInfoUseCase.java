package com.example.megohike.domain.use_case;

import androidx.lifecycle.LiveData;

import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;

import java.util.List;

public interface LoadAllHikeInfoUseCase {
    List<HikeInfo> getAllHikeInfo();
}
