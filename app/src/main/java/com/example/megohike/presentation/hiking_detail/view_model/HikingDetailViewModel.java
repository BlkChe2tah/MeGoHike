package com.example.megohike.presentation.hiking_detail.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.megohike.common.UiState;
import com.example.megohike.common.UiStateEmpty;
import com.example.megohike.common.UiStateSuccess;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.domain.use_case.DeleteEquipmentUseCase;
import com.example.megohike.domain.use_case.DeleteHikeInfoUseCase;
import com.example.megohike.domain.use_case.DeleteObservationUseCase;
import com.example.megohike.domain.use_case.HikeInfoDetailUseCase;
import com.example.megohike.domain.use_case.LoadAllEquipmentsUseCase;
import com.example.megohike.domain.use_case.LoadAllHikeInfoUseCase;
import com.example.megohike.domain.use_case.LoadAllObservationsUseCase;

import java.util.ArrayList;
import java.util.List;

public class HikingDetailViewModel extends ViewModel {

    private final LoadAllObservationsUseCase observationsUseCase;
    private final LoadAllEquipmentsUseCase equipmentsUseCase;
    private final DeleteHikeInfoUseCase deleteUseCase;
    private final DeleteEquipmentUseCase deleteEquipmentUseCase;
    private final DeleteObservationUseCase deleteObservationUseCase;
    private final HikeInfoDetailUseCase hikeInfoDetailUseCase;

    private final MutableLiveData<HikeInfo> _hikeInfo = new MutableLiveData<>(null);
    public final LiveData<HikeInfo> hikeInfo = _hikeInfo;

    private final MutableLiveData<List<Observation>> _observations = new MutableLiveData<>();
    public final LiveData<List<Observation>> getObservations = _observations;
    private final MutableLiveData<List<Equipment>> equipments = new MutableLiveData<>();
    public final LiveData<List<Equipment>> getEquipments = equipments;

    final private MutableLiveData<Boolean> uiState = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiState = uiState;

    final private MutableLiveData<Boolean> uiStateDelete = new MutableLiveData<>(null);
    final public LiveData<Boolean> getUiStateDelete = uiStateDelete;

    public HikingDetailViewModel(@NonNull LoadAllObservationsUseCase observationsUseCase, @NonNull LoadAllEquipmentsUseCase equipmentsUseCase, @NonNull DeleteHikeInfoUseCase deleteUseCase, @NonNull DeleteEquipmentUseCase deleteEquipmentUseCase, @NonNull DeleteObservationUseCase DeleteObservationUseCase, @NonNull HikeInfoDetailUseCase hikeInfoDetailUseCase) {
        this.observationsUseCase = observationsUseCase;
        this.equipmentsUseCase = equipmentsUseCase;
        this.deleteUseCase = deleteUseCase;
        this.deleteEquipmentUseCase = deleteEquipmentUseCase;
        this.deleteObservationUseCase = DeleteObservationUseCase;
        this.hikeInfoDetailUseCase = hikeInfoDetailUseCase;
    }

    public void loadDetail(int hikeId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            try {
                loadEquipments(hikeId);
                loadObservations(hikeId);
                loadHikeInfo(hikeId);
                uiState.postValue(true);
            } catch (Exception e) {
                uiState.postValue(false);
            }
        });
    }

    public void deleteEquipment(int hikeId, int equipmentId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            try {
                deleteEquipmentUseCase.deleteEquipment(equipmentId);
                loadEquipments(hikeId);
            } catch (Exception e) {
            }
        });
    }

    public void deleteObservation(int hikeId, int observationId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            try {
                deleteObservationUseCase.deleteObservation(observationId);
                loadObservations(hikeId);
            } catch (Exception e) {
            }
        });
    }

    public void delete(int hikeId) {
        HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
            try {
                deleteUseCase.deleteHikeInfo(hikeId);
                uiStateDelete.postValue(true);
            } catch (Exception e) {
                uiStateDelete.postValue(false);
            }
        });
    }

    public void loadObservations(int hikeId) {
        final List<Observation> observationItems = new ArrayList<>();
        observationItems.add(null);
        final List<Observation> result = observationsUseCase.getAllObservationsByHikeId(hikeId);
        if (result != null && result.size() != 0) {
            observationItems.addAll(result);
            _observations.postValue(observationItems);
        } else {
            _observations.postValue(observationItems);
        }
    }

    public void loadHikeInfo(int hikeId) {
        final HikeInfo result = hikeInfoDetailUseCase.loadHikeInfo(hikeId);
        _hikeInfo.postValue(result);
    }

    public void loadEquipments(int hikeId) {
        final List<Equipment> result = equipmentsUseCase.getAllEquipmentsByHikeId(hikeId);
        if (result != null && result.size() != 0) {
            equipments.postValue(result);
        } else {
            equipments.postValue(null);
        }
    }

}
