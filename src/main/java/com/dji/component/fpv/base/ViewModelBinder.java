package com.dji.component.fpv.base;

import com.dji.component.fpv.base.AbstractViewModel;

public interface ViewModelBinder<T extends AbstractViewModel> {
    void bind(AbstractViewModel abstractViewModel);
}
