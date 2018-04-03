package com.pharbers.memory

import com.pharbers.jobs.PhJobs

trait PhMemory[T] {
    def registerStorage(job : PhJobs, des : String, storage : T)
}

trait PhReuseMemory[T] extends PhMemory[T] {
    def queryStorage(job : PhJobs, des : String) : T
}
